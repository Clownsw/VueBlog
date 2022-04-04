use actix_web::{get, post, web, HttpRequest, Responder};
use chrono::Utc;
use vueblog_common::{
    dao::blog_dao::{add_blog, get_by_id, select_all_count, select_all_limit, update_blog_by_id, delete_blog_by_id},
    pojo::{
        blog::{InsertBlog, LimitBlogs, RequestBlog, SelectBlog, UpdateBlog},
        msg::ResultMsg,
        status::AppState,
    },
    util::{error_util, sql_util::sql_run_is_success},
};

use crate::util::login_util::is_login_return;

/**
 * 所有文章
 */
#[get("/blogs")]
pub async fn blog_list(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    let mut current: i64 = 1;

    let _tmp: Vec<&str> = req.query_string().split("=").collect();

    if _tmp.len() >= 2 {
        match _tmp[1].parse::<i64>() {
            Ok(v) => {
                current = v;
            }
            Err(_) => {}
        }
    }

    let blogs = select_all_limit(&data.db_pool, (current - 1) * 5, 5).await;
    let counts = select_all_count(&data.db_pool).await.unwrap();

    match blogs {
        Ok(v) => serde_json::to_string(&ResultMsg::<LimitBlogs<SelectBlog>>::success_all(
            200,
            Some(String::from(error_util::SUCCESS)),
            Some(LimitBlogs::from_unknown_datas(counts[0].count, current, v)),
        ))
        .unwrap(),
        Err(_) => String::from("null"),
    }
}

/**
 * 查询文章
 */
#[get("/blog/{id}")]
pub async fn blog_detail(path: web::Path<i64>, data: web::Data<AppState>) -> impl Responder {
    let id = path.into_inner();

    match get_by_id(&data.db_pool, id).await {
        Ok(v) => serde_json::to_string(&v).unwrap(),
        Err(_) => serde_json::to_string(&ResultMsg::<()>::fail_msg(Some(String::from(
            error_util::BLOG_HAS_DELETE,
        ))))
        .unwrap(),
    }
}

/**
 * 删除文章
 */
#[post("/blog/remove/{id}")]
pub async fn blog_delete(
    path: web::Path<i64>,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    let id = path.into_inner();

    let (user, error_str) = is_login_return(&req, &data.db_pool).await;
    if let Some(v) = error_str {
        return v;
    }

    let user = user.unwrap();

    match get_by_id(&data.db_pool, id).await {
        Ok(v) => {
            // 如果文章的创建者, 不是当前传入的token的用户, 直接返回错误信息
            if v.user_id == user.id {
                if sql_run_is_success(delete_blog_by_id(&data.db_pool, id).await).await {
                    return serde_json::to_string(&ResultMsg::<()>::success_message(Some(
                        String::from(error_util::SUCCESS),
                    )))
                    .unwrap();
                }
            }

            serde_json::to_string(&ResultMsg::<()>::fail_msg(Some(String::from(
                error_util::NOT_REQUEST_ACCESS,
            ))))
            .unwrap()
        }
        Err(_) => serde_json::to_string(&ResultMsg::<()>::fail_msg(Some(String::from(
            error_util::BLOG_HAS_DELETE,
        ))))
        .unwrap(),
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
        return v;
    }

    let user = user.unwrap();

    match serde_json::from_str::<RequestBlog>(body.as_str()) {
        Ok(v) => {
            println!("{:?}", v);

            // 编辑
            if let Some(id) = v.id {
                match get_by_id(&data.db_pool, id).await {
                    Ok(v2) => {
                        // 没有权限编辑
                        if v2.user_id != user.id {
                            return serde_json::to_string(&ResultMsg::<()>::fail_msg(Some(
                                String::from(error_util::NOT_EDIT_ACCESS),
                            )))
                            .unwrap();
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
                            return serde_json::to_string(&ResultMsg::<()>::success_message(Some(
                                String::from(error_util::SUCCESS),
                            )))
                            .unwrap();
                        }

                        return serde_json::to_string(&ResultMsg::<()>::fail_msg(Some(
                            String::from(error_util::ERROR),
                        )))
                        .unwrap();
                    }
                    Err(_) => {
                        // 找不到这个文章
                        return serde_json::to_string(&ResultMsg::<()>::fail_msg(Some(
                            String::from(error_util::BLOG_HAS_DELETE),
                        )))
                        .unwrap();
                    }
                }
            } else {
                // 添加
                let insert_blog = InsertBlog {
                    user_id: user.id,
                    title: v.title,
                    description: v.description,
                    content: v.content,
                    created: Utc::now().naive_utc(),
                    status: 0,
                };

                if sql_run_is_success(add_blog(&data.db_pool, insert_blog).await).await {
                    return serde_json::to_string(&ResultMsg::<()>::success_message(Some(
                        String::from(error_util::SUCCESS),
                    )))
                    .unwrap();
                }

                return serde_json::to_string(&ResultMsg::<()>::fail_msg(Some(String::from(
                    error_util::SUCCESS,
                ))))
                .unwrap();
            }
        }
        Err(_) => serde_json::to_string(&ResultMsg::<()>::fail_msg(Some(String::from(
            error_util::INCOMPLETE_REQUEST,
        ))))
        .unwrap(),
    }
}
