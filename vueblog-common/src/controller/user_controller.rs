use crate::{
    dao::user_dao::{get_by_id, select_all_user, update_by_id},
    pojo::{
        msg::ResultMsg,
        status::AppState,
        user::{ResponseUser, SelectUser, UpdateUser},
    },
    util::{common_util::to_json_string, error_util, login_util::is_login_return},
};
use log::info;
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
    let (_, error_msg) = is_login_return(&req, &data.db_pool).await;
    if let Some(v) = error_msg {
        info!("未登录");
        return to_json_string(&ResultMsg::<()>::fail_msg(Some(v))).await;
    }

    info!("登录了");

    match serde_json::from_str::<UpdateUser>(body.as_str()) {
        Ok(v) => {
            if let Ok(v) = update_by_id(&data.db_pool, v).await {
                if v.rows_affected() > 0 {
                    return to_json_string(&ResultMsg::<()>::success_message(Some(String::from(
                        error_util::SUCCESS,
                    ))))
                    .await;
                }
            }
        }
        Err(_) => {}
    }

    to_json_string(&ResultMsg::<()>::fail_msg(Some(String::from(
        error_util::INCOMPLETE_REQUEST,
    ))))
    .await
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
