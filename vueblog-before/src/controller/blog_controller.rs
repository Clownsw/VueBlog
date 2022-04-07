use actix_web::{get, web, Responder};
use vueblog_common::{
    dao::blog_dao::get_by_id,
    pojo::status::AppState,
    util::common_util::{build_response_baq_request_message, build_response_ok_data},
    util::error_util,
};

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
