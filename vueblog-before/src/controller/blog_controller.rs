use actix_web::{get, web, Responder};
use vueblog_common::{
    dao::blog_dao::get_by_id,
    pojo::{msg::ResultMsg, status::AppState},
    util::error_util,
};

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
