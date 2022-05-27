use actix_web::{get, post, web, HttpRequest, Responder};

use crate::{
    dao::{
        blog_dao,
        sort_dao::{add_sort, delete_by_id, select_all, update_by_id},
    },
    pojo::{
        other::Void,
        sort::{InsertSort, SelectSort, UpdateSort},
        status::AppState,
    },
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
 * 所有分类
 */
#[get("/sort/list")]
pub async fn sort_list(data: web::Data<AppState>) -> impl Responder {
    match select_all(&data.db_pool).await {
        Ok(v) => build_response_ok_data(v).await,
        _ => build_response_ok_data::<Vec<SelectSort>>(vec![]).await,
    }
}

/**
 * 添加分类
 */
#[post("/sort/add")]
pub async fn sort_add(body: String, req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, body, _, _| {
            let body = body.unwrap();
            let db_pool_clone = app_state.db_pool.clone();

            Box::pin(async move {
                let mut r = build_response_baq_request_message(String::from(
                    error_util::ERROR_REQUEST_PARAM,
                ))
                .await;

                match serde_json::from_str::<InsertSort>(body.as_str()) {
                    Ok(v) => {
                        if sql_run_is_success(add_sort(&db_pool_clone, v).await).await {
                            r = build_response_ok_message(String::from(error_util::SUCCESS)).await;
                        }
                    }
                    _ => {}
                }

                r
            })
        },
        &req,
        &data,
        Some(body),
        None,
    )
    .await
}

#[post("/sort/update")]
pub async fn sort_update(
    body: String,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, body, _, _| {
            let body = body.unwrap();
            let db_pool_clone = app_state.db_pool.clone();

            Box::pin(async move {
                let mut r = build_response_baq_request_message(String::from(
                    error_util::ERROR_REQUEST_PARAM,
                ))
                .await;

                match serde_json::from_str::<UpdateSort>(body.as_str()) {
                    Ok(v) => {
                        if sql_run_is_success(update_by_id(&db_pool_clone, v).await).await {
                            r = build_response_ok_message(String::from(error_util::SUCCESS)).await;
                        }
                    }
                    _ => {}
                }

                r
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
 * 通过ID删除分类
 */
#[get("/sort/remove/{id}")]
pub async fn sort_remove(
    id: web::Path<i32>,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    security_interceptor_aop::<_, i32>(
        move |app_state, _, data, _| {
            let id = data.unwrap();
            let db_pool_clone = app_state.db_pool.clone();

            Box::pin(async move {
                // 分类迁移
                blog_dao::update_blog_sort_to_new_by_sort_id(&db_pool_clone, id, 1)
                    .await
                    .unwrap();

                if sql_run_is_success(delete_by_id(&db_pool_clone, id).await).await {
                    return build_response_ok_message(String::from(error_util::SUCCESS)).await;
                }

                build_response_ok_message(String::from(error_util::ERROR_UNKNOWN)).await
            })
        },
        &req,
        &data,
        None,
        Some(id.into_inner()),
    )
    .await
}
