use crate::{
    dao::blog_dao::{delete_by_ids, select_all_count, select_all_limit},
    pojo::{
        blog::{LimitBlogs, SelectBlog},
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
        Ok(v) => {
            build_response_ok_data::<LimitBlogs<SelectBlog>>(LimitBlogs::from_unknown_datas(
                counts[0].count,
                current,
                v,
            ))
            .await
        }
        Err(_) => build_response_ok_message(String::from("null")).await,
    }
}

/**
 * 批量删除博文
 */
#[post("/blog/removes/")]
pub async fn blog_removes(
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
