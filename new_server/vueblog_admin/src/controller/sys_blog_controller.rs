use actix_web::Responder;
use crate::domain::vo::RespVO;
use crate::service::CONTEXT;

pub async fn all() -> impl Responder {
    RespVO::from_result(&CONTEXT.sys_blog_service.select_all().await).resp_json()
}

pub async fn page() -> impl Responder {
    RespVO::from_result(&CONTEXT.sys_blog_service.select_page().await).resp_json()
}