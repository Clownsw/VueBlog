use crate::{
    dao::{
        blog_tag_dao::delete_all_blog_by_tag_id,
        tag_dao::{delete_by_id, insert_tag, select_all, select_all_by_blog_id, update_by_id},
    },
    pojo::{
        status::AppState,
        tag::{InsertTag, UpdateTag},
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
    let (_, error_msg) = is_login_return(&req, &data.db_pool).await;
    if let Some(_) = error_msg {
        return build_response_baq_request_message(String::from(error_util::NOT_REQUEST_ACCESS))
            .await;
    }

    if let Ok(v) = serde_json::from_str::<UpdateTag>(body.as_str()) {
        if sql_run_is_success(update_by_id(&data.db_pool, v).await).await {
            return build_response_ok_message(String::from(error_util::SUCCESS)).await;
        }
    }

    build_response_baq_request().await
}

/**
 * 添加一个标签
 */
#[post("/tag/add")]
pub async fn tag_add(body: String, req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    let (_, error_msg) = is_login_return(&req, &data.db_pool).await;
    if let Some(_) = error_msg {
        return build_response_baq_request_message(String::from(error_util::NOT_REQUEST_ACCESS))
            .await;
    }

    if let Ok(v) = serde_json::from_str::<InsertTag>(body.as_str()) {
        if sql_run_is_success(insert_tag(&data.db_pool, v).await).await {
            return build_response_ok_message(String::from(error_util::SUCCESS)).await;
        }
    }

    build_response_baq_request().await
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
    let (_, error_msg) = is_login_return(&req, &data.db_pool).await;
    if let Some(_) = error_msg {
        return build_response_baq_request_message(String::from(error_util::NOT_REQUEST_ACCESS))
            .await;
    }

    let id = path.into_inner();

    sql_run_is_success(delete_all_blog_by_tag_id(&data.db_pool, id).await).await;

    if sql_run_is_success(delete_by_id(&data.db_pool, id).await).await {
        return build_response_ok_message(String::from(error_util::SUCCESS)).await;
    }

    build_response_baq_request().await
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
