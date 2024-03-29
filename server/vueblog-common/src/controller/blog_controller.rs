use actix_web::{get, post, web, HttpRequest, Responder};
use qstring::QString;

use crate::{
    config::global_config::GLOBAL_CONFIG,
    dao::{
        blog_dao::{
            delete_by_ids_tran, delete_key_by_ids_tran, get_blog_key_by_id, get_by_id_and_key,
            get_by_id_with_sort_and_tag, select_all_count, select_all_limit,
            select_all_limit_by_sort_id, select_all_limit_by_tag_id, select_sort_all_count,
            select_tag_all_count,
        },
        staticstics_dao::blog_view_count_plus,
    },
    pojo::{
        blog::{RequestBlog, SelectBlogSortTag, SelectCountBlog},
        limit::Limit,
        other::{SelectCount, Void},
        status::AppState,
    },
    service::blog_service::{blog_add_service, blog_update_service},
    util::{
        common_util::{
            build_response_baq_request_message, build_response_ok_data, build_response_ok_message,
            security_interceptor_aop,
        },
        error_util,
        login_util::is_login_return,
        sql_util::sql_run_is_success,
    },
};

/**
 * 所有文章
 */
#[get("/blogs")]
pub async fn blog_list(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    let global_config = GLOBAL_CONFIG.get().unwrap();
    let mut currnet_page: i64 = 1;
    let mut page_size: i64 = global_config.blog_limit_num;

    let qs = QString::from(req.query_string());

    if let Some(v) = qs.get("currentPage") {
        if let Ok(v) = v.parse::<i64>() {
            currnet_page = v;
        }
    }

    if let Some(v) = qs.get("pageSize") {
        if let Ok(v) = v.parse::<i64>() {
            page_size = v;
        }
    }

    let is_login = is_login_return(&req, &data.db_pool).await.1 == None;

    let blogs = select_all_limit(
        &data.db_pool,
        (currnet_page - 1) * page_size,
        page_size,
        is_login,
    )
    .await;

    let select_count = match select_all_count(&data.db_pool, qs.get("queryStr")).await {
        Ok(v) => v,
        _ => SelectCount::default(),
    };

    match blogs {
        Ok(v) => {
            build_response_ok_data(Limit::from_unknown_datas(
                global_config.blog_limit_num,
                select_count.count,
                currnet_page,
                v,
            ))
            .await
        }
        Err(_) => build_response_ok_message(String::from("null")).await,
    }
}

/**
 * 通过文章ID和key查询文章
 */
#[get("/blog/key/{id}/{key}")]
pub async fn blog_detail_id_key(
    path: web::Path<(i64, String)>,
    data: web::Data<AppState>,
) -> impl Responder {
    let (id, key) = path.into_inner();

    match get_by_id_and_key(&data.db_pool, id, key.as_str()).await {
        Ok(v) => build_response_ok_data(v).await,
        _ => build_response_baq_request_message(String::from(error_util::ERROR)).await,
    }
}

/**
 * 通过文章ID获取key
 */
#[get("/blog/key/{id}")]
pub async fn blog_detail_key(
    path: web::Path<i64>,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    let id = path.into_inner();

    security_interceptor_aop::<_, Void>(
        move |app_state, _, _, _| {
            let db_pool_clone = app_state.db_pool.clone();

            Box::pin(async move {
                match get_blog_key_by_id(&db_pool_clone, id).await {
                    Ok(v) => build_response_ok_data(v).await,
                    _ => build_response_baq_request_message(String::from(error_util::ERROR)).await,
                }
            })
        },
        &req,
        &data,
        None,
        None,
    )
    .await
}

/**
 * 查询文章
 */
#[get("/blog/{id}")]
pub async fn blog_detail(
    path: web::Path<i64>,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    let id = path.into_inner();

    let is_login = is_login_return(&req, &data.db_pool).await.1 == None;

    match get_by_id_with_sort_and_tag(&data.db_pool, id).await {
        Ok(mut v) => {
            let mut conn = data.redis_pool.get().await.unwrap();
            blog_view_count_plus(&mut conn).await;

            // 博文开启了加密
            if v.status == 1 {
                // 如果不是来自后台 则清空内容
                if !is_login {
                    v.content = String::new();
                }
            }

            build_response_ok_data(v).await
        }
        Err(_) => {
            build_response_baq_request_message(String::from(error_util::BLOG_HAS_DELETE)).await
        }
    }
}

/**
 * 分页查询指定分类下的所有文章
 */
#[get("/blogs/sort/list")]
pub async fn blog_sort_list(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    let mut current: i64 = 1;
    let mut sort_id = 1;

    let qs = QString::from(req.query_string());

    if let Some(v) = qs.get("currentPage") {
        if let Ok(v) = v.parse::<i64>() {
            current = v;
        }
    }

    if let Some(v) = qs.get("sortId") {
        if let Ok(v) = v.parse::<i32>() {
            sort_id = v;
        }
    }

    let global_config = GLOBAL_CONFIG.get().unwrap();
    let blogs = select_all_limit_by_sort_id(
        &data.db_pool,
        (current - 1) * global_config.blog_sort_limit_num,
        global_config.blog_sort_limit_num,
        sort_id,
    )
    .await;

    let count = match select_sort_all_count(&data.db_pool, sort_id).await {
        Ok(v) => v,
        _ => SelectCountBlog { count: 0 },
    };

    if let Ok(v) = blogs {
        build_response_ok_data(Limit::from_unknown_datas(
            global_config.blog_sort_limit_num,
            count.count,
            current,
            v,
        ))
        .await
    } else {
        build_response_ok_data(Vec::<SelectBlogSortTag>::new()).await
    }
}

/**
 * 分页查询指定标签下的所有文章
 */
#[get("/blogs/tag/list")]
pub async fn blog_tag_list(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    let mut current: i64 = 1;
    let mut tag_id: i64 = 1;

    let qs = QString::from(req.query_string());

    if let Some(v) = qs.get("currentPage") {
        if let Ok(v) = v.parse::<i64>() {
            current = v;
        }
    }

    if let Some(v) = qs.get("tagId") {
        if let Ok(v) = v.parse::<i64>() {
            tag_id = v;
        }
    }

    let global_config = GLOBAL_CONFIG.get().unwrap();
    let blogs = select_all_limit_by_tag_id(
        &data.db_pool,
        (current - 1) * global_config.blog_tag_limit_num,
        global_config.blog_tag_limit_num,
        tag_id,
    )
    .await;

    let count = match select_tag_all_count(&data.db_pool, tag_id).await {
        Ok(v) => v,
        _ => SelectCountBlog { count: 0 },
    };

    if let Ok(v) = blogs {
        build_response_ok_data(Limit::from_unknown_datas(
            global_config.blog_tag_limit_num,
            count.count,
            current,
            v,
        ))
        .await
    } else {
        build_response_ok_data(Vec::<SelectBlogSortTag>::new()).await
    }
}

/**
 * 编辑、添加文章
 */
#[post("/blog/edit")]
pub async fn blog_edit(
    body: String,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, body, _, user| {
            let db_pool_clone = app_state.db_pool.clone();
            let blog_index = app_state.search_client.index("blog");
            let body = body.unwrap();

            Box::pin(async move {
                match serde_json::from_str::<RequestBlog>(body.as_str()) {
                    Ok(v) => {
                        let mut resp =
                            build_response_baq_request_message(String::from(error_util::ERROR))
                                .await;
                        // 编辑
                        if let Some(id) = v.id {
                            let ids = v.tag.iter().map(|item| item.id).collect::<Vec<i64>>();

                            if blog_update_service(&db_pool_clone, v, id, ids, blog_index).await {
                                resp = build_response_ok_message(String::from(error_util::SUCCESS))
                                    .await;
                            }
                        } else {
                            // 添加
                            if blog_add_service(&db_pool_clone, user.id, v, blog_index).await {
                                resp = build_response_ok_message(String::from(error_util::SUCCESS))
                                    .await;
                            }
                        }

                        resp
                    }
                    Err(_) => {
                        build_response_baq_request_message(String::from(
                            error_util::INCOMPLETE_REQUEST,
                        ))
                        .await
                    }
                }
            })
        },
        &req,
        &data,
        Some(body),
        None,
    )
    .await
}

/**
 * 批量删除博文
 */
#[post("/blog/deletes")]
pub async fn blog_deletes(
    body: String,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, body, _, _| {
            let db_pool_clone = app_state.db_pool.clone();
            let blog_index = app_state.search_client.index("blog");
            let body = body.unwrap();

            Box::pin(async move {
                let ids: Vec<i64> = serde_json::from_str(body.as_str()).unwrap();

                let mut transaction = db_pool_clone.begin().await.unwrap();

                if let Err(_) = delete_key_by_ids_tran(&mut transaction, ids.clone()).await {}
                if !sql_run_is_success(delete_by_ids_tran(&mut transaction, ids.clone()).await)
                    .await
                {
                    transaction.rollback().await.unwrap();
                    return build_response_baq_request_message(String::from(
                        error_util::ERROR_UNKNOWN,
                    ))
                    .await;
                }

                blog_index.delete_documents(&ids).await.unwrap();
                transaction.commit().await.unwrap();
                build_response_ok_message(String::from(error_util::SUCCESS)).await
            })
        },
        &req,
        &data,
        Some(body),
        None,
    )
    .await
}
