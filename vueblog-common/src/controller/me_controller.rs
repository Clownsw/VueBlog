use actix_web::{get, post, web, HttpRequest, HttpResponse, Responder};

use crate::{
    dao::other_dao::{select_me_by_user_id, update_me_by_id},
    pojo::{me::UpdateMe, other::Void, status::AppState},
    util::{
        common_util::{
            build_response_baq_request_message, build_response_ok_data, build_response_ok_message,
            security_interceptor_aop,
        },
        error_util,
        sql_util::sql_run_is_success,
    },
};

/**
 * 获取关于我
 */
#[get("/me")]
pub async fn me(data: web::Data<AppState>) -> impl Responder {
    match select_me_by_user_id(&data.db_pool).await {
        Ok(v) => build_response_ok_data(v).await,
        Err(_) => build_response_ok_message(String::from(error_util::ERROR_UNKNOWN)).await,
    }
}

/**
 * 更新关于我
 */
#[post("/me/update")]
pub async fn me_update(
    body: String,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, body, _, _| {
            let db_pool_clone = app_state.db_pool.clone();
            let body = body.unwrap();

            Box::pin(async move {
                let mut resp: HttpResponse<String> =
                    build_response_baq_request_message(String::from(error_util::ERROR)).await;

                match serde_json::from_str::<UpdateMe>(body.as_str()) {
                    Ok(v) => {
                        if sql_run_is_success(update_me_by_id(&db_pool_clone, v).await).await {
                            resp =
                                build_response_ok_message(String::from(error_util::SUCCESS)).await;
                        }
                    }
                    _ => {}
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
