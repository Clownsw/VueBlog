use crate::{
    dao::system_dao::{select_system_info, update_system_info},
    pojo::{status::AppState, system::UpdateSystem},
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
 * 获取系统信息
 */
#[get("/system/info")]
pub async fn system_info(data: web::Data<AppState>) -> impl Responder {
    match select_system_info(&data.db_pool).await {
        Ok(v) => build_response_ok_data(v).await,
        Err(_) => build_response_baq_request_message(String::from(error_util::ERROR_UNKNOWN)).await,
    }
}

/**
 * 更新系统信息
 */
#[post("/system/update")]
pub async fn system_update(
    body: String,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    let (_, error_msg) = is_login_return(&req, &data.db_pool).await;

    if let Some(_) = error_msg {
        return build_response_baq_request_message(String::from(error_util::NOT_REQUEST_ACCESS))
            .await;
    }

    match serde_json::from_str::<UpdateSystem>(body.as_str()) {
        Ok(v) => {
            if sql_run_is_success(update_system_info(&data.db_pool, v).await).await {
                return build_response_ok_message(String::from(error_util::SUCCESS)).await;
            }
        }
        Err(_) => {}
    }

    build_response_baq_request_message(String::from(error_util::INCOMPLETE_REQUEST)).await
}
