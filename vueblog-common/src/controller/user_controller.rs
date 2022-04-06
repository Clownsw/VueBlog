use crate::{
    dao::user_dao::{delete_by_id, get_by_id, select_all_user, update_by_id},
    pojo::{
        msg::ResultMsg,
        status::AppState,
        user::{ResponseUser, SelectUser, UpdateUser},
    },
    util::{
        common_util::{
            build_response_baq_request, build_response_baq_request_message,
            build_response_ok_message, to_json_string,
        },
        error_util,
        login_util::is_login_return,
        sql_util::sql_run_is_success,
    },
};
use actix_web::{get, post, web, HttpRequest, Responder};

/**
 * 获取所有用户并序列化为JSON返回
 */
#[post("/user/all")]
pub async fn all_user(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    let (_, error_msg) = is_login_return(&req, &data.db_pool).await;
    if let Some(v) = error_msg {
        return to_json_string(&ResultMsg::<()>::fail_msg(Some(v))).await;
    }

    let all_user = select_all_user(&data.db_pool).await.unwrap();

    to_json_string(&ResultMsg::<Vec<SelectUser>>::success(Some(all_user))).await
}

/**
 * 获取用户信息
 */
#[post("/user/info")]
pub async fn user_info(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    let (user, error_msg) = is_login_return(&req, &data.db_pool).await;
    if let Some(v) = error_msg {
        return to_json_string(&ResultMsg::<()>::fail_msg(Some(v))).await;
    }

    let user = user.unwrap();

    match get_by_id(&data.db_pool, user.id).await {
        Ok(v) => {
            to_json_string(&ResultMsg::<ResponseUser>::success_all(
                200,
                Some(String::from(error_util::SUCCESS)),
                Some(ResponseUser::from_select_user(String::new(), v)),
            ))
            .await
        }
        Err(_) => {
            to_json_string(&ResultMsg::<()>::fail_msg(Some(String::from(
                error_util::NOT_FOUND_USER,
            ))))
            .await
        }
    }
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
    // 验证用户是否登录
    let (_, error_msg) = is_login_return(&req, &data.db_pool).await;
    if let Some(v) = error_msg {
        return build_response_baq_request_message(v).await;
    }

    // 序列化JSON, 如果失败直接返回400
    match serde_json::from_str::<UpdateUser>(body.as_str()) {
        Ok(v) => {
            if sql_run_is_success(update_by_id(&data.db_pool, v).await).await {
                return build_response_ok_message(String::from(error_util::SUCCESS)).await;
            }
        }
        Err(_) => {}
    }

    build_response_baq_request_message(String::from(error_util::INCOMPLETE_REQUEST)).await
}

/**
 * 删除用户
 */
#[post("/user/delete/{id}")]
pub async fn user_delete(
    path: web::Path<i64>,
    req: HttpRequest,
    data: web::Data<AppState>,
) -> impl Responder {
    let id = path.into_inner();

    // 验证用户是否登录
    let (_, error_msg) = is_login_return(&req, &data.db_pool).await;
    if let Some(v) = error_msg {
        return build_response_baq_request_message(v).await;
    }

    if sql_run_is_success(delete_by_id(&data.db_pool, id).await).await {
        return build_response_ok_message(String::from(error_util::SUCCESS)).await;
    }

    build_response_baq_request().await
}

#[get("/user/index")]
pub async fn index(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    let (user, error_msg) = is_login_return(&req, &data.db_pool).await;
    if let Some(v) = error_msg {
        return to_json_string(&ResultMsg::<()>::fail_msg(Some(v))).await;
    }

    let user = user.unwrap();

    to_json_string(&user).await
}
