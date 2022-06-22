use actix_web::{post, web, Responder};
use chrono::Utc;

use crate::{
    dao::user_dao::{get_by_name_and_passwd, update_user_last_login_by_id},
    pojo::{
        claims::Claims,
        msg::ResultMsg,
        other::Void,
        status::AppState,
        token::Token,
        user::{LoginUser, TokenUser},
    },
    util::{
        common_util::{build_response_baq_request_message, build_response_ok_data},
        error_util,
        jwt_util::{get_token_default_token_user, sign_token_default},
    },
};

/**
 * 登录API
 */
#[post("/admin/login")]
pub async fn login(body: String, data: web::Data<AppState>) -> impl Responder {
    let login_user: LoginUser;

    // 将请求体序列化为LoginUser
    // 如果序列化失败, 直接返回提示错误
    match serde_json::from_str::<LoginUser>(body.as_str()) {
        Ok(v) => {
            login_user = v;
        }
        Err(_) => {
            return build_response_baq_request_message(String::from(
                error_util::NOT_FOUND_USERNAME_OR_PASSWORD,
            ))
            .await;
        }
    };

    let mut resp =
        build_response_baq_request_message(String::from(error_util::ERROR_USERNAME_OR_PASSWORD))
            .await;

    // 验证账户密码的合法性
    match get_by_name_and_passwd(
        &data.db_pool,
        login_user.username.as_str(),
        login_user.password.as_str(),
    )
    .await
    {
        // 如果合法则生成token并返回
        Ok(v) => {
            // 校验用户是否被锁定
            if v.status != -1 {
                // 更新最后登录时间
                update_user_last_login_by_id(&data.db_pool, v.id, Utc::now().naive_local())
                    .await
                    .unwrap();

                let token_user = TokenUser::from_select_user(v.clone());
                let token = Token {
                    token: get_token_default_token_user(Some(token_user)).await,
                };

                resp = build_response_ok_data(token).await;
            } else {
                resp = build_response_baq_request_message(String::from(
                    error_util::USER_STATUS_UNAVAILABLE,
                ))
                .await
            }
        }
        // 找不到直接返回相关错误提示
        _ => {}
    }

    resp
}

/**
 * 验证token是否有效
 */
#[post("/token")]
pub async fn sign_token(body: String) -> impl Responder {
    match sign_token_default::<Claims<TokenUser>>(body.as_str()).await {
        Ok(v) => {
            if v.claims.exp > Utc::now().timestamp_millis() as usize {
                return build_response_ok_data(ResultMsg::<Void> {
                    code: 200,
                    message: Some(String::from(error_util::SUCCESS)),
                    data: Some(Void::new()),
                })
                .await;
            }
        }
        Err(_) => {}
    }

    build_response_ok_data(ResultMsg::<Void> {
        code: 400,
        message: Some(String::from(error_util::NOT_REQUEST_ACCESS)),
        data: Some(Void::new()),
    })
    .await
}
