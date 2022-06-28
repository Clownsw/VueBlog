use crate::{
    dao::{
        blog_tag_dao::delete_all_blog_by_tag_id,
        tag_dao::{
            delete_by_id, insert_tag, select_all, select_all_by_blog_id, select_id_by_name,
            update_by_id,
        },
    },
    pojo::{
        other::Void,
        status::AppState,
        tag::{InsertTag, SelectTag, UpdateTag},
    },
    util::{
        common_util::{
            build_response_baq_request, build_response_ok_data, build_response_ok_message,
            security_interceptor_aop,
        },
        error_util,
        sql_util::sql_run_is_success,
    },
};
use actix_web::{get, post, web, HttpRequest, Responder};

/**
 * 获取所有标签
 */
#[get("/tags")]
pub async fn tag_all(data: web::Data<AppState>) -> impl Responder {
    match select_all(&data.db_pool).await {
        Ok(v) => build_response_ok_data(v).await,
        Err(_) => build_response_baq_request().await,
    }
}

/**
 * 更新一个标签
 */
#[post("/tag/update")]
pub async fn tag_update(
    body: String,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, body, _, _| {
            let db_pool_clone = app_state.db_pool.clone();
            let body = body.unwrap();

            Box::pin(async move {
                if let Ok(v) = serde_json::from_str::<UpdateTag>(body.as_str()) {
                    if sql_run_is_success(update_by_id(&db_pool_clone, v).await).await {
                        return build_response_ok_message(String::from(error_util::SUCCESS)).await;
                    }
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
 * 添加一个标签
 */
#[post("/tag/add")]
pub async fn tag_add(body: String, req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, body, _, _| {
            let db_pool_clone = app_state.db_pool.clone();
            let body = body.unwrap();

            Box::pin(async move {
                if let Ok(v) = serde_json::from_str::<InsertTag>(body.as_str()) {
                    if sql_run_is_success(insert_tag(&db_pool_clone, v).await).await {
                        return build_response_ok_message(String::from(error_util::SUCCESS)).await;
                    }
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
 * 删除一个标签
 */
#[get("/tag/delete/{id}")]
pub async fn tag_delete(
    path: web::Path<i64>,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    security_interceptor_aop::<_, web::Path<i64>>(
        move |app_state, _, path, _| {
            let db_pool_clone = app_state.db_pool.clone();

            Box::pin(async move {
                let id = path.unwrap().into_inner();

                // 如果有博客使用这个标签, 则先删除该记录
                sql_run_is_success(delete_all_blog_by_tag_id(&db_pool_clone, id).await).await;

                // 删除标签
                if sql_run_is_success(delete_by_id(&db_pool_clone, id).await).await {
                    return build_response_ok_message(String::from(error_util::SUCCESS)).await;
                }

                build_response_baq_request().await
            })
        },
        &req,
        &data,
        None,
        Some(path),
    )
    .await
}

/**
 * 获取博文的所有标签
 */
#[get("/tag/{id}")]
pub async fn tags_blog(path: web::Path<i64>, data: web::Data<AppState>) -> impl Responder {
    let id = path.into_inner();

    match select_all_by_blog_id(&data.db_pool, id).await {
        Ok(v) => build_response_ok_data(v).await,
        Err(_) => build_response_baq_request().await,
    }
}

/**
 * 获取标签名称
 */
#[get("/tag/id/{name}")]
pub async fn tag_id_by_name(path: web::Path<String>, data: web::Data<AppState>) -> impl Responder {
    let name = path.into_inner();

    match select_id_by_name(&data.db_pool, name).await {
        Ok(v) => build_response_ok_data(v).await,
        _ => build_response_ok_data(SelectTag::new()).await,
    }
}

/**
 * 标签是否存在
 */
#[get("/tag/exist/{name}")]
pub async fn tag_is_exist(path: web::Path<String>, data: web::Data<AppState>) -> impl Responder {
    let name = path.into_inner();

    match select_id_by_name(&data.db_pool, name).await {
        Ok(_) => build_response_ok_data(true).await,
        _ => build_response_ok_data(false).await,
    }
}
