use crate::{
    dao::user_dao::{get_by_id, select_all_user},
    pojo::{msg::ResultMsg, status::AppState, user::ResponseUser},
    util::{common_util::to_json_string, error_util, login_util::is_login_return},
};
use actix_web::{get, post, web, HttpRequest, Responder};

/**
 * 获取所有用户并序列化为JSON返回
 */
#[get("/user/all")]
pub async fn all_user(data: web::Data<AppState>) -> impl Responder {
    let all_user = select_all_user(&data.db_pool).await.unwrap();

    to_json_string(&all_user).await
}

/**
 * 获取用户信息
 */
#[post("/user/info")]
pub async fn user_info(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    let (user, error_msg) = is_login_return(&req, &data.db_pool).await;
    if let Some(v) = error_msg {
        return v;
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

#[get("/user/index")]
pub async fn index(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    let (user, error_msg) = is_login_return(&req, &data.db_pool).await;
    if let Some(v) = error_msg {
        return v;
    }

    let user = user.unwrap();

    to_json_string(&user).await
}
