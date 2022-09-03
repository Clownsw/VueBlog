use actix_web::{Responder, web};

use crate::domain::{dto::blog::BlogPageDTO, vo::RespVO};
use crate::service::CONTEXT;

pub async fn blog_page(page: web::Json<BlogPageDTO>) -> impl Responder {
    RespVO::from_result(&CONTEXT.sys_blog_service.select_id_page(page.0.into()).await).resp_json()
}
