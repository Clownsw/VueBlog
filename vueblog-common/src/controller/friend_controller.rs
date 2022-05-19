use crate::{
    config::global_config::PAGE_LIMIT_NUM,
    dao::friend_dao::{
        delete_by_ids, insert_friend, select_all, select_all_count, select_all_limit, update_friend,
    },
    pojo::{
        friend::{InsertFriend, UpdateFriend},
        limit::Limit,
        other::Void,
        status::AppState,
    },
    util::{
        common_util::{
            build_response_baq_request, build_response_baq_request_message, build_response_ok_data,
            build_response_ok_message, security_interceptor_aop,
        },
        error_util,
        sql_util::sql_run_is_success,
    },
};
use actix_web::{get, post, web, HttpRequest, Responder};
use qstring::QString;

/**
 * 获取所有友联信息
 */
#[get("/friends")]
pub async fn friend_all(data: web::Data<AppState>) -> impl Responder {
    match select_all(&data.db_pool).await {
        Ok(v) => build_response_ok_data(v).await,
        Err(_) => build_response_baq_request().await,
    }
}

/**
 * 分页获取友联信息
 */
#[get("/friend/limit")]
pub async fn friend_limit(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    let mut current = 1;

    let qs = QString::from(req.query_string());

    if let Some(v) = qs.get("currentPage") {
        if let Ok(v) = v.parse::<i64>() {
            current = v;
        }
    }

    let friends = select_all_limit(
        &data.db_pool,
        (current - 1) * PAGE_LIMIT_NUM,
        PAGE_LIMIT_NUM,
    )
    .await;

    let counts = select_all_count(&data.db_pool).await.unwrap();

    match friends {
        Ok(v) => {
            build_response_ok_data(Limit::from_unknown_datas(counts[0].count, current, v)).await
        }
        Err(_) => {
            build_response_baq_request_message(String::from(error_util::INCOMPLETE_REQUEST)).await
        }
    }
}

/**
 * 添加一个友联
 */
#[post("/friend/add")]
pub async fn friend_add(
    body: String,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, body, _, _| {
            let db_pool_clone = app_state.db_pool.clone();
            let body = body.unwrap();

            Box::pin(async move {
                match serde_json::from_str::<InsertFriend>(body.as_str()) {
                    Ok(v) => {
                        if sql_run_is_success(insert_friend(&db_pool_clone, v).await).await {
                            return build_response_ok_message(String::from(error_util::SUCCESS))
                                .await;
                        }
                    }
                    Err(_) => {}
                }

                build_response_baq_request().await
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
 * 更新一个友联
 */
#[post("/friend/update")]
pub async fn friend_update(
    body: String,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, body, _, _| {
            let db_pool_clone = app_state.db_pool.clone();
            let body = body.unwrap();

            Box::pin(async move {
                match serde_json::from_str::<UpdateFriend>(body.as_str()) {
                    Ok(v) => {
                        if sql_run_is_success(update_friend(&db_pool_clone, v).await).await {
                            return build_response_ok_message(String::from(error_util::SUCCESS))
                                .await;
                        }
                    }
                    Err(_) => {}
                }

                build_response_baq_request().await
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
 * 批量删除友链
 */
#[post("/friend/deletes")]
pub async fn friend_deletes(
    body: String,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, body, _, _| {
            let db_pool_clone = app_state.db_pool.clone();
            let body = body.unwrap();

            Box::pin(async move {
                if let Ok(v) = serde_json::from_str::<Vec<i64>>(body.as_str()) {
                    if sql_run_is_success(delete_by_ids(&db_pool_clone, v).await).await {
                        return build_response_ok_message(String::from(error_util::SUCCESS)).await;
                    }
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
