use actix_web::{get, web, Responder};
use vueblog_common::{
    dao::blog_dao::get_by_id,
    pojo::{msg::ResultMsg, status::AppState},
    util::common_util::to_json_string,
    util::error_util,
};

/**
 * 查询文章
 */
#[get("/blog/{id}")]
pub async fn blog_detail(path: web::Path<i64>, data: web::Data<AppState>) -> impl Responder {
    let id = path.into_inner();

    match get_by_id(&data.db_pool, id).await {
        Ok(v) => to_json_string(&v).await,
        Err(_) => {
            to_json_string(&ResultMsg::<()>::fail_msg(Some(String::from(
                error_util::BLOG_HAS_DELETE,
            ))))
            .await
        }
    }
}
