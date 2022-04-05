use crate::{
    dao::user_dao::select_all_user,
    pojo::status::AppState,
    util::{common_util::to_json_string, login_util::is_login_return},
};
use actix_web::{get, web, HttpRequest, Responder};

/**
 * 获取所有用户并序列化为JSON返回
 */
#[get("/user/all")]
pub async fn all_user(data: web::Data<AppState>) -> impl Responder {
    let all_user = select_all_user(&data.db_pool).await.unwrap();

    to_json_string(&all_user).await
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
