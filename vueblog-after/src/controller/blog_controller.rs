use actix_web::{post, web, HttpRequest, Responder};
use chrono::Utc;
use vueblog_common::{
    dao::blog_dao::{add_blog, delete_blog_by_id, get_by_id, update_blog_by_id},
    pojo::{
        blog::{InsertBlog, RequestBlog, UpdateBlog},
        msg::ResultMsg,
        status::AppState,
    },
    util::{
        common_util::to_json_string, error_util, login_util::is_login_return,
        sql_util::sql_run_is_success,
    },
};

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
        return to_json_string(&ResultMsg::<()>::fail_msg(Some(v))).await;
    }

    let user = user.unwrap();

    match get_by_id(&data.db_pool, id).await {
        Ok(v) => {
            // 如果文章的创建者, 不是当前传入的token的用户, 直接返回错误信息
            if v.user_id == user.id {
                if sql_run_is_success(delete_blog_by_id(&data.db_pool, id).await).await {
                    return to_json_string(&ResultMsg::<()>::success_message(Some(String::from(
                        error_util::SUCCESS,
                    ))))
                    .await;
                }
            }

            to_json_string(&ResultMsg::<()>::fail_msg(Some(String::from(
                error_util::NOT_REQUEST_ACCESS,
            ))))
            .await
        }
        Err(_) => {
            to_json_string(&ResultMsg::<()>::fail_msg(Some(String::from(
                error_util::BLOG_HAS_DELETE,
            ))))
            .await
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
        return to_json_string(&ResultMsg::<()>::fail_msg(Some(v))).await;
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
                            return to_json_string(&ResultMsg::<()>::fail_msg(Some(String::from(
                                error_util::NOT_EDIT_ACCESS,
                            ))))
                            .await;
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
                            return to_json_string(&ResultMsg::<()>::success_message(Some(
                                String::from(error_util::SUCCESS),
                            )))
                            .await;
                        }

                        return to_json_string(&ResultMsg::<()>::fail_msg(Some(String::from(
                            error_util::ERROR,
                        ))))
                        .await;
                    }
                    Err(_) => {
                        // 找不到这个文章
                        return to_json_string(&ResultMsg::<()>::fail_msg(Some(String::from(
                            error_util::BLOG_HAS_DELETE,
                        ))))
                        .await;
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
                    return to_json_string(&ResultMsg::<()>::success_message(Some(String::from(
                        error_util::SUCCESS,
                    ))))
                    .await;
                }

                return to_json_string(&ResultMsg::<()>::fail_msg(Some(String::from(
                    error_util::SUCCESS,
                ))))
                .await;
            }
        }
        Err(_) => {
            to_json_string(&ResultMsg::<()>::fail_msg(Some(String::from(
                error_util::INCOMPLETE_REQUEST,
            ))))
            .await
        }
    }
}
