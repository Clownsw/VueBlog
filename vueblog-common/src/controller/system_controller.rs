use crate::{
    dao::system_dao::{select_system_info, update_system_info},
    pojo::{
        status::AppState,
        system::{SelectSystem, UpdateSystem},
    },
    util::{
        common_util::{
            build_response_baq_request_message, build_response_ok_data, build_response_ok_message,
            to_json_string,
        },
        error_util,
        login_util::is_login_return,
        redis_util,
        sql_util::sql_run_is_success,
    },
};
use actix_web::{get, post, web, HttpRequest, Responder};

/**
 * 获取系统信息
 */
#[get("/system/info")]
pub async fn system_info(data: web::Data<AppState>) -> impl Responder {
    let mut async_conn = data.redis_pool.get().await.unwrap();
    let mut r: Option<SelectSystem> = None;

    // 如果redis中存在, 则直接获取
    if let Ok(v) = redis_util::get::<String, _>(&mut async_conn, "system_info").await {
        r = Some(serde_json::from_str(v.as_str()).unwrap());
    } else if let Ok(v) = select_system_info(&data.db_pool).await {
        // 到这里说明redis中没有, 存一下
        redis_util::set(&mut async_conn, "system_info", to_json_string(&v).await).await;

        r = Some(v);
    }

    if let Some(v) = r {
        return build_response_ok_data(v).await;
    }

    build_response_baq_request_message(String::from(error_util::ERROR_UNKNOWN)).await
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
