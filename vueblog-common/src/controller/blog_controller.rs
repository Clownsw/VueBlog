use crate::{
    config::global_config::PAGE_LIMIT_NUM,
    dao::{
        blog_dao::{
            add_blog, delete_by_ids, get_by_id, get_by_id_with_sort_and_tag, select_all_count,
            select_all_limit, select_all_limit_by_sort_id, select_all_limit_by_tag_id,
            select_by_title, select_sort_all_count, select_tag_all_count, update_blog_by_id,
        },
        blog_tag_dao::{add_blog_tag_by_ids, delete_blog_tag_by_tag_ids},
        tag_dao::select_all_by_blog_id,
    },
    pojo::{
        blog::{InsertBlog, RequestBlog, SelectBlogSortTag, SelectCountBlog, UpdateBlog},
        limit::Limit,
        other::Void,
        status::AppState,
    },
    util::{
        common_util::{
            build_response_baq_request_message, build_response_ok_data, build_response_ok_message,
            get_del_and_add_vec, security_interceptor_aop,
        },
        error_util,
        sql_util::sql_run_is_success,
    },
};
use actix_web::{get, post, web, HttpRequest, Responder};
use chrono::Utc;
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

    let blogs = select_all_limit(
        &data.db_pool,
        (current - 1) * PAGE_LIMIT_NUM,
        PAGE_LIMIT_NUM,
    )
    .await;
    
    let counts = match select_all_count(&data.db_pool).await {
        Ok(v) => v,
        _ => Vec::new(),
    };

    match blogs {
        Ok(v) => {
            build_response_ok_data(Limit::from_unknown_datas(counts[0].count, current, v)).await
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
        build_response_ok_data(Limit::from_unknown_datas(count.count, current, v)).await
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
        build_response_ok_data(Limit::from_unknown_datas(count.count, current, v)).await
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

                        // 编辑
                        if let Some(id) = v.id {
                            match get_by_id(&db_pool_clone, id).await {
                                Ok(_) => {
                                    if v.tag.len() > 0 {
                                        match select_all_by_blog_id(&db_pool_clone, id).await {
                                            Ok(v2) => {
                                                let old_ids: Vec<i64> = v2
                                                    .iter()
                                                    .map(|item| item.id.unwrap())
                                                    .collect();

                                                println!("old_ids: {:?}", old_ids);

                                                let (dels, adds) =
                                                    get_del_and_add_vec(old_ids, ids).await;
                                                let mut result: Vec<bool> = vec![];

                                                if dels.len() > 0 {
                                                    result.push(
                                                        sql_run_is_success(
                                                            delete_blog_tag_by_tag_ids(
                                                                &db_pool_clone,
                                                                id,
                                                                dels,
                                                            )
                                                            .await,
                                                        )
                                                        .await,
                                                    );
                                                } else if adds.len() > 0 {
                                                    result.push(
                                                        sql_run_is_success(
                                                            add_blog_tag_by_ids(
                                                                &db_pool_clone,
                                                                id,
                                                                adds,
                                                            )
                                                            .await,
                                                        )
                                                        .await,
                                                    );
                                                }

                                                for r in result {
                                                    if !r {
                                                        return build_response_baq_request_message(
                                                            String::from(error_util::ERROR),
                                                        )
                                                        .await;
                                                    }
                                                }
                                            }
                                            _ => {}
                                        }
                                    }

                                    let update_blog = UpdateBlog {
                                        id: v.id.unwrap(),
                                        sort_id: v.sort_id,
                                        title: v.title,
                                        content: v.content,
                                        description: v.description,
                                    };

                                    if sql_run_is_success(
                                        update_blog_by_id(&db_pool_clone, update_blog).await,
                                    )
                                    .await
                                    {
                                        return build_response_ok_message(String::from(
                                            error_util::SUCCESS,
                                        ))
                                        .await;
                                    }

                                    return build_response_baq_request_message(String::from(
                                        error_util::ERROR,
                                    ))
                                    .await;
                                }
                                Err(_) => {
                                    // 找不到这个文章
                                    return build_response_baq_request_message(String::from(
                                        error_util::BLOG_HAS_DELETE,
                                    ))
                                    .await;
                                }
                            }
                        } else {
                            // 添加
                            let title = v.title.clone();

                            let insert_blog = InsertBlog {
                                user_id: user.id,
                                sort_id: v.sort_id,
                                title: v.title,
                                description: v.description,
                                content: v.content,
                                created: Utc::now().naive_utc(),
                                status: 0,
                            };

                            if sql_run_is_success(add_blog(&db_pool_clone, insert_blog).await).await
                            {
                                if let Ok(v2) = select_by_title(&db_pool_clone, title).await {
                                    if v.tag.len() > 0 {
                                        sql_run_is_success(
                                            add_blog_tag_by_ids(&db_pool_clone, v2.id, ids).await,
                                        )
                                        .await;
                                    }
                                    return build_response_ok_message(String::from(
                                        error_util::SUCCESS,
                                    ))
                                    .await;
                                }
                            }

                            return build_response_baq_request_message(String::from(
                                error_util::SUCCESS,
                            ))
                            .await;
                        }
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
#[post("/blog/deletes/")]
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
