use actix_web::{post, Responder, web};

use crate::pojo::status::AppState;
use crate::util::common_util::build_response_ok_message;

#[post("/search")]
pub async fn search_blog(body: String, data: web::Data<AppState>) -> impl Responder {
    println!("body: {}", body);
    build_response_ok_message(data.search_client.get_version().await.unwrap().pkg_version).await
}