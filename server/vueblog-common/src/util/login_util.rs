use actix_web::{http::header, HttpRequest};
use serde::de::DeserializeOwned;
use sqlx::MySqlPool;

use crate::{
    dao::user_dao::get_by_id,
    pojo::{
        claims::Claims,
        user::{SelectUser, TokenUser},
    },
};

use super::{
    error_util,
    jwt_util::{is_token_expried, sign_token_default},
};

/**
 * 判断是否登录
 */
pub async fn is_login<T: std::cmp::PartialEq + DeserializeOwned>(
    req: &HttpRequest,
) -> (Option<Claims<T>>, bool) {
    match req.headers().get(header::AUTHORIZATION) {
        Some(v) => {
            match sign_token_default::<Claims<T>>(String::from(v.to_str().unwrap()).trim()).await {
                Ok(v) => {
                    if is_token_expried(&v.claims).await {
                        (Some(v.claims), true)
                    } else {
                        (None, false)
                    }
                }
                Err(_) => (None, false),
            }
        }
        None => (None, false),
    }
}

/**
 * 是否登录
 * 如果没有登录, 返回错误信息
 * 反之返回用户信息
 */
pub async fn is_login_return(
    req: &HttpRequest,
    db_pool: &MySqlPool,
) -> (Option<SelectUser>, Option<String>) {
    let (login_info, login_status) = is_login::<TokenUser>(&req).await;

    if login_status {
        if let Some(v) = login_info {
            let user = v.data.unwrap();
            if let Ok(v) = get_by_id(&db_pool, user.id).await {
                if v.status != -1 && v.status == user.status {
                    return (Some(v), None);
                }

                return (
                    None,
                    Some(String::from(error_util::USER_STATUS_UNAVAILABLE)),
                );
            }
        }
    }

    (None, Some(String::from(error_util::NOT_REQUEST_ACCESS)))
}
