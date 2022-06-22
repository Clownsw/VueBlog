use actix_web::{get, post, web, HttpRequest, Responder};
use reqwest::StatusCode;

use crate::{
    pojo::{backup::SelectBackUp, other::Void, status::AppState},
    service::backup_service::{get_backup_info, update_backup_info},
    util::{
        common_util::{
            build_response_baq_request_message, build_response_ok_data, build_response_ok_message,
            dump_sql, remote_upload_file, security_interceptor_aop,
        },
        error_util,
    },
};

#[get("/backup/info")]
pub async fn backup_info(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, _, _, _| {
            let db_pool_clone = app_state.db_pool.clone();

            Box::pin(async move {
                let backup = get_backup_info(&db_pool_clone).await;
                build_response_ok_data(backup).await
            })
        },
        &req,
        &data,
        None,
        None,
    )
    .await
}

#[post("/backup/update")]
pub async fn backup_update(
    body: String,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, body, _, _| {
            let body = body.unwrap();
            let db_pool_clone = app_state.db_pool.clone();

            Box::pin(async move {
                let mut resp =
                    build_response_baq_request_message(String::from(error_util::ERROR)).await;

                if let Ok(v) = serde_json::from_str::<SelectBackUp>(body.as_str()) {
                    if update_backup_info(&db_pool_clone, v).await {
                        resp = build_response_ok_message(String::from(error_util::SUCCESS)).await;
                    }
                }

                resp
            })
        },
        &req,
        &data,
        Some(body),
        None,
    )
    .await
}

#[get("/backup/buy")]
pub async fn backup_buy(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, _, _, _| {
            let db_pool_clone = app_state.db_pool.clone();

            Box::pin(async move {
                let mut resp =
                    build_response_baq_request_message(String::from(error_util::ERROR)).await;
                let backup = get_backup_info(&db_pool_clone).await;

                if let Ok(v) = dump_sql(backup.clone()).await {
                    if let Ok(v) = remote_upload_file(backup, v).await {
                        if v == StatusCode::OK {
                            resp =
                                build_response_ok_message(String::from(error_util::SUCCESS)).await;
                        }
                    }
                }
                resp
            })
        },
        &req,
        &data,
        None,
        None,
    )
    .await
}
