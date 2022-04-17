use crate::{
    config::global_config::PAGE_LIMIT_NUM,
    dao::{
        blog_dao::{
            add_blog, delete_by_ids, get_by_id, select_all_count, select_all_limit,
            select_by_title, update_blog_by_id,
        },
        blog_tag_dao::{add_blog_tag_by_ids, delete_all_by_blog_id},
    },
    pojo::{
        blog::{InsertBlog, RequestBlog, UpdateBlog},
        limit::Limit,
        status::AppState,
    },
    util::{
        common_util::{
            build_response_baq_request_message, build_response_ok_data, build_response_ok_message,
        },
        error_util,
        login_util::is_login_return,
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
    let counts = select_all_count(&data.db_pool).await.unwrap();

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

    match get_by_id(&data.db_pool, id).await {
        Ok(v) => build_response_ok_data(v).await,
        Err(_) => {
            build_response_baq_request_message(String::from(error_util::BLOG_HAS_DELETE)).await
        }
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
    let (user, error_str) = is_login_return(&req, &data.db_pool).await;
    if let Some(v) = error_str {
        return build_response_baq_request_message(v).await;
    }

    let user = user.unwrap();

    match serde_json::from_str::<RequestBlog>(body.as_str()) {
        Ok(v) => {
            let ids = v.tag.iter().map(|item| item.id).collect::<Vec<i64>>();

            // 编辑
            if let Some(id) = v.id {
                match get_by_id(&data.db_pool, id).await {
                    Ok(_) => {
                        if v.tag.len() > 0 {
                            sql_run_is_success(
                                delete_all_by_blog_id(&data.db_pool, v.id.unwrap()).await,
                            )
                            .await;

                            if !sql_run_is_success(
                                add_blog_tag_by_ids(&data.db_pool, v.id.unwrap(), ids).await,
                            )
                            .await
                            {
                                return build_response_baq_request_message(String::from(
                                    error_util::ERROR_UNKNOWN,
                                ))
                                .await;
                            }
                        }

                        let update_blog = UpdateBlog {
                            id: v.id.unwrap(),
                            title: v.title,
                            content: v.content,
                            description: v.description,
                        };

                        if sql_run_is_success(update_blog_by_id(&data.db_pool, update_blog).await)
                            .await
                        {
                            return build_response_ok_message(String::from(error_util::SUCCESS))
                                .await;
                        }

                        return build_response_baq_request_message(String::from(error_util::ERROR))
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
                    title: v.title,
                    description: v.description,
                    content: v.content,
                    created: Utc::now().naive_utc(),
                    status: 0,
                };

                if sql_run_is_success(add_blog(&data.db_pool, insert_blog).await).await {
                    if let Ok(v2) = select_by_title(&data.db_pool, title).await {
                        if v.tag.len() > 0 {
                            sql_run_is_success(
                                add_blog_tag_by_ids(&data.db_pool, v2.id, ids).await,
                            )
                            .await;
                        }
                        return build_response_ok_message(String::from(error_util::SUCCESS)).await;
                    }
                }

                return build_response_baq_request_message(String::from(error_util::SUCCESS)).await;
            }
        }
        Err(_) => {
            build_response_baq_request_message(String::from(error_util::INCOMPLETE_REQUEST)).await
        }
    }
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
    let ids: Vec<i64> = serde_json::from_str(body.as_str()).unwrap();

    let (_, error_msg) = is_login_return(&req, &data.db_pool).await;
    if let None = error_msg {
        if sql_run_is_success(delete_by_ids(&data.db_pool, ids).await).await {
            return build_response_ok_message(String::from(error_util::SUCCESS)).await;
        }
    }

    build_response_baq_request_message(String::from(error_util::ERROR_UNKNOWN)).await
}

/**
 * 关于我
 */
#[get("/blog/me")]
pub async fn blog_me(data: web::Data<AppState>) -> impl Responder {
    match get_by_id(&data.db_pool, 1 as i64).await {
        Ok(v) => build_response_ok_data(v).await,
        Err(_) => {
            build_response_baq_request_message(String::from(error_util::BLOG_HAS_DELETE)).await
        }
    }
}
