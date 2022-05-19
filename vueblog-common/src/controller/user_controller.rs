use crate::{
    dao::user_dao::{delete_by_ids, get_by_id, insert_user, select_all_user, update_by_id},
    pojo::{
        other::Void,
        status::AppState,
        user::{InsertUser, ResponseUser, SelectUser, UpdateUser},
    },
    util::{
        common_util::{
            build_response_baq_request_message, build_response_ok_data,
            build_response_ok_data_message, build_response_ok_message, security_interceptor_aop,
        },
        error_util,
        sql_util::sql_run_is_success,
    },
};
use actix_web::{get, post, web, HttpRequest, Responder};

/**
 * 获取所有用户并序列化为JSON返回
 */
#[post("/user/all")]
pub async fn all_user(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, _, _, _| {
            let db_pool_clone = app_state.db_pool.clone();

            Box::pin(async move {
                let all_user = select_all_user(&db_pool_clone).await.unwrap();
                build_response_ok_data::<Vec<SelectUser>>(all_user).await
            })
        },
        &req,
        &data,
        None,
        None,
    )
    .await
}

/**
 * 获取用户信息
 */
#[post("/user/info")]
pub async fn user_info(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, _, _, user| {
            let db_pool_clone = app_state.db_pool.clone();

            Box::pin(async move {
                match get_by_id(&db_pool_clone, user.id).await {
                    Ok(v) => {
                        build_response_ok_data_message::<ResponseUser>(
                            ResponseUser::from_select_user(String::new(), v),
                            String::from(error_util::SUCCESS),
                        )
                        .await
                    }
                    Err(_) => {
                        build_response_baq_request_message(String::from(error_util::NOT_FOUND_USER))
                            .await
                    }
                }
            })
        },
        &req,
        &data,
        None,
        None,
    )
    .await
}

/**
 * 更新用户信息
 */
#[post("/user/update")]
pub async fn user_update(
    body: String,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, body, _, _| {
            let db_pool_clone = app_state.db_pool.clone();
            let body = body.unwrap();

            Box::pin(async move {
                // 序列化JSON, 如果失败直接返回400
                match serde_json::from_str::<UpdateUser>(body.as_str()) {
                    Ok(v) => {
                        if sql_run_is_success(update_by_id(&db_pool_clone, v).await).await {
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
 * 添加用户
 */
#[post("/user/insert")]
pub async fn user_add(body: String, req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, body, _, _| {
            let db_pool_clone = app_state.db_pool.clone();
            let body = body.unwrap();

            Box::pin(async move {
                match serde_json::from_str::<InsertUser>(body.as_str()) {
                    Ok(v) => {
                        if sql_run_is_success(insert_user(&db_pool_clone, v).await).await {
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
 * 批量删除用户
 */
#[post("/user/deletes/")]
pub async fn user_deletes(
    body: String,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, body, _, _| {
            let db_pool_clone = app_state.db_pool.clone();
            let body = body.unwrap();

            Box::pin(async move {
                let ids: Vec<i64> = serde_json::from_str(body.as_str()).unwrap();

                if sql_run_is_success(delete_by_ids(&db_pool_clone, ids).await).await {
                    return build_response_ok_message(String::from(error_util::SUCCESS)).await;
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

#[get("/user/index")]
pub async fn index(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |_, _, _, user| Box::pin(async move { build_response_ok_data(user).await }),
        &req,
        &data,
        None,
        None,
    )
    .await
}
