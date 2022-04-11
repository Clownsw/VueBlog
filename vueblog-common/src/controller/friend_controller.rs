use crate::{
    config::global_config::PAGE_LIMIT_NUM,
    dao::friend_dao::{
        delete_by_ids, insert_friend, select_all, select_all_count, select_all_limit, update_friend,
    },
    pojo::{
        friend::{InsertFriend, UpdateFriend},
        limit::Limit,
        status::AppState,
    },
    util::{
        common_util::{
            build_response_baq_request, build_response_baq_request_message, build_response_ok_data,
            build_response_ok_message,
        },
        error_util,
        login_util::is_login_return,
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
    let (_, error_msg) = is_login_return(&req, &data.db_pool).await;

    if let Some(_) = error_msg {
        return build_response_baq_request_message(String::from(error_util::NOT_REQUEST_ACCESS))
            .await;
    }

    match serde_json::from_str::<InsertFriend>(body.as_str()) {
        Ok(v) => {
            if sql_run_is_success(insert_friend(&data.db_pool, v).await).await {
                return build_response_ok_message(String::from(error_util::SUCCESS)).await;
            }
        }
        Err(_) => {}
    }

    build_response_baq_request().await
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
    let (_, error_msg) = is_login_return(&req, &data.db_pool).await;

    if let Some(_) = error_msg {
        return build_response_baq_request_message(String::from(error_util::NOT_REQUEST_ACCESS))
            .await;
    }

    match serde_json::from_str::<UpdateFriend>(body.as_str()) {
        Ok(v) => {
            if sql_run_is_success(update_friend(&data.db_pool, v).await).await {
                return build_response_ok_message(String::from(error_util::SUCCESS)).await;
            }
        }
        Err(_) => {}
    }

    build_response_baq_request().await
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
    let (_, error_msg) = is_login_return(&req, &data.db_pool).await;

    if let Some(_) = error_msg {
        return build_response_baq_request_message(String::from(error_util::NOT_REQUEST_ACCESS))
            .await;
    }

    if let Ok(v) = serde_json::from_str::<Vec<i64>>(body.as_str()) {
        if sql_run_is_success(delete_by_ids(&data.db_pool, v).await).await {
            return build_response_ok_message(String::from(error_util::SUCCESS)).await;
        }
    }

    build_response_baq_request_message(String::from(error_util::INCOMPLETE_REQUEST)).await
}
