use crate::{
    config::global_config::PAGE_LIMIT_NUM,
    dao::blog_dao::{
        delete_by_ids, get_by_id_with_sort_and_tag, select_all_count, select_all_limit,
        select_all_limit_by_sort_id, select_all_limit_by_tag_id, select_sort_all_count,
        select_tag_all_count,
    },
    pojo::{
        blog::{RequestBlog, SelectBlogSortTag, SelectCountBlog},
        limit::Limit,
        other::Void,
        status::AppState,
    },
    service::blog_service::{blog_add_service, blog_update_service},
    util::{
        common_util::{
            build_response_baq_request_message, build_response_ok_data, build_response_ok_message,
            security_interceptor_aop,
        },
        error_util,
        sql_util::sql_run_is_success,
    },
};
use actix_web::{get, post, web, HttpRequest, Responder};
use qstring::QString;

/**
 * 所有文章
 */
#[get("/blogs")]
pub async fn blog_list(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    let mut current: i64 = 1;

    let qs = QString::from(req.query_string());

    if let Some(v) = qs.get("currentPage") {
        if let Ok(v) = v.parse::<i64>() {
            current = v;
        }
    }

    let blogs = select_all_limit(&data.db_pool, (current - 1) * 10, 10).await;

    let counts = match select_all_count(&data.db_pool).await {
        Ok(v) => v,
        _ => Vec::new(),
    };

    match blogs {
        Ok(v) => {
            build_response_ok_data(Limit::from_unknown_datas(10, counts[0].count, current, v)).await
        }
        Err(_) => build_response_ok_message(String::from("null")).await,
    }
}

/**
 * 查询文章
 */
#[get("/blog/{id}")]
pub async fn blog_detail(path: web::Path<i64>, data: web::Data<AppState>) -> impl Responder {
    let id = path.into_inner();

    match get_by_id_with_sort_and_tag(&data.db_pool, id).await {
        Ok(v) => build_response_ok_data(v).await,
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

    let blogs = select_all_limit_by_sort_id(
        &data.db_pool,
        (current - 1) * PAGE_LIMIT_NUM,
        PAGE_LIMIT_NUM,
        sort_id,
    )
    .await;

    let count = match select_sort_all_count(&data.db_pool, sort_id).await {
        Ok(v) => v,
        _ => SelectCountBlog { count: 0 },
    };

    if let Ok(v) = blogs {
        build_response_ok_data(Limit::from_unknown_datas(
            PAGE_LIMIT_NUM,
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

    let blogs = select_all_limit_by_tag_id(
        &data.db_pool,
        (current - 1) * PAGE_LIMIT_NUM,
        PAGE_LIMIT_NUM,
        tag_id,
    )
    .await;

    let count = match select_tag_all_count(&data.db_pool, tag_id).await {
        Ok(v) => v,
        _ => SelectCountBlog { count: 0 },
    };

    if let Ok(v) = blogs {
        build_response_ok_data(Limit::from_unknown_datas(
            PAGE_LIMIT_NUM,
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
            let body = body.unwrap();

            Box::pin(async move {
                match serde_json::from_str::<RequestBlog>(body.as_str()) {
                    Ok(v) => {
                        let ids = v.tag.iter().map(|item| item.id).collect::<Vec<i64>>();

                        let mut resp =
                            build_response_baq_request_message(String::from(error_util::ERROR))
                                .await;
                        // 编辑
                        if let Some(id) = v.id {
                            if blog_update_service(&db_pool_clone, v, id, ids).await {
                                resp = build_response_ok_message(String::from(error_util::SUCCESS))
                                    .await;
                            }
                        } else {
                            // 添加
                            if blog_add_service(&db_pool_clone, user.id, v, ids).await {
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
            let body = body.unwrap();

            Box::pin(async move {
                let ids: Vec<i64> = serde_json::from_str(body.as_str()).unwrap();

                if sql_run_is_success(delete_by_ids(&db_pool_clone, ids).await).await {
                    return build_response_ok_message(String::from(error_util::SUCCESS)).await;
                }
                build_response_baq_request_message(String::from(error_util::ERROR_UNKNOWN)).await
            })
        },
        &req,
        &data,
        Some(body),
        None,
    )
    .await
}
