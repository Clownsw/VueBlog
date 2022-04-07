use crate::{
    dao::blog_dao::{select_all_count, select_all_limit},
    pojo::{
        blog::{LimitBlogs, SelectBlog},
        status::AppState,
    },
    util::common_util::{build_response_ok_data, build_response_ok_message},
};
use actix_web::{get, web, HttpRequest, Responder};

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
