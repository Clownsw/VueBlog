use crate::{
    dao::tag_dao::select_all_by_blog_id,
    pojo::status::AppState,
    util::common_util::{build_response_baq_request, build_response_ok_data},
};
use actix_web::{get, web, Responder};

/**
 * 获取博文的所有标签
 */
#[get("/tag/{id}")]
pub async fn tags_blog(path: web::Path<i64>, data: web::Data<AppState>) -> impl Responder {
    let id = path.into_inner();

    match select_all_by_blog_id(&data.db_pool, id).await {
        Ok(v) => build_response_ok_data(v).await,
        Err(_) => build_response_baq_request().await,
    }
}