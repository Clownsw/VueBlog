use crate::{
    dao::{
        other_dao::{select_page_footer, update_page_footer},
        system_dao::{select_system_info, update_system_info},
    },
    pojo::{
        other::{UpdatePageFooter, Void},
        status::AppState,
        system::{SelectSystem, UpdateSystem},
    },
    util::{
        common_util::{
            build_response_baq_request_message, build_response_ok_data, build_response_ok_message,
            security_interceptor_aop, to_json_string,
        },
        error_util, redis_util,
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
    security_interceptor_aop::<_, Void>(
        move |app_state, body, _, _| {
            let db_pool_clone = app_state.db_pool.clone();
            let redis_pool_clone = app_state.redis_pool.clone();
            let body = body.unwrap();

            Box::pin(async move {
                match serde_json::from_str::<UpdateSystem>(body.as_str()) {
                    Ok(v) => {
                        if sql_run_is_success(update_system_info(&db_pool_clone, v.clone()).await)
                            .await
                        {
                            let mut async_conn = redis_pool_clone.get().await.unwrap();

                            // 刷新redis中的system_info
                            redis_util::update(
                                &mut async_conn,
                                "system_info",
                                to_json_string(&v).await,
                            )
                            .await;

                            return build_response_ok_message(String::from(error_util::SUCCESS))
                                .await;
                        }
                    }
                    Err(_) => {}
                }

                build_response_baq_request_message(String::from(error_util::INCOMPLETE_REQUEST))
                    .await
            })
        },
        &req,
        &data,
        Some(body),
        None,
    )
    .await
}

/**
 * 获取page footer
 */
#[get("/footer")]
pub async fn page_footer(data: web::Data<AppState>) -> impl Responder {
    match select_page_footer(&data.db_pool).await {
        Ok(v) => build_response_ok_data(v).await,
        _ => build_response_ok_data(String::new()).await,
    }
}

/**
 * 更新page footer
 */
#[post("/footer/update")]
pub async fn page_footer_update(
    body: String,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, body, _, _| {
            let db_pool_clone = app_state.db_pool.clone();
            let body = body.unwrap();

            Box::pin(async move {
                let mut resp =
                    build_response_baq_request_message(String::from(error_util::ERROR)).await;

                match serde_json::from_str::<UpdatePageFooter>(body.as_str()) {
                    Ok(v) => {
                        if sql_run_is_success(update_page_footer(&db_pool_clone, v).await).await {
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
