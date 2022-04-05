use actix_web::{http::StatusCode, post, web, HttpRequest, Responder};
use chrono::Utc;
use log::{error, info};
use vueblog_common::{
    dao::user_dao::get_by_name_and_passwd,
    pojo::{
        claims::Claims,
        msg::ResultMsg,
        status::AppState,
        user::{LoginUser, ResponseUser, TokenUser},
    },
    util::{
        common_util::{build_http_response_json, sign_captcha_code, to_json_string},
        error_util,
        jwt_util::{get_token_default_token_user, sign_token_default},
        redis_util,
    },
};

/**
 * 登录API
 */
#[post("/admin/login")]
pub async fn login(body: String, req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    let login_user: LoginUser;

    // 将请求体序列化为LoginUser
    // 如果序列化失败, 直接返回提示错误
    match serde_json::from_str::<LoginUser>(body.as_str()) {
        Ok(v) => {
            login_user = v;
        }
        Err(_) => {
            return build_http_response_json(StatusCode::BAD_REQUEST)
                .await
                .set_body(
                    to_json_string(&ResultMsg::<()>::fail_msg(Some(String::from(
                        error_util::NOT_FOUND_USERNAME_OR_PASSWORD,
                    ))))
                    .await,
                );
        }
    };

    println!("login_user={:?}", login_user);
    let mut async_conn = data.redis_pool.as_ref().unwrap().get().await.unwrap();

    // 验证验证码正确性
    if sign_captcha_code(
        &mut async_conn,
        login_user.captcha_id.clone(),
        login_user.captcha_code.clone(),
    )
    .await
    {
        redis_util::delete(&mut async_conn, login_user.captcha_id.clone()).await;

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
                    info!(
                        "登录成功: IP={}, 用户名={}, 密码={}",
                        req.connection_info().realip_remote_addr().unwrap(),
                        login_user.username,
                        login_user.password
                    );

                    println!("query_user = {:?}", v);
                    let token_user = TokenUser::from_select_user(v.clone());
                    let token = get_token_default_token_user(Some(token_user)).await;

                    let response_user = ResponseUser::from_select_user(token, v);

                    return build_http_response_json(StatusCode::OK).await.set_body(
                        to_json_string(&ResultMsg::<ResponseUser>::success_all(
                            200,
                            Some(String::from(error_util::SUCCESS)),
                            Some(response_user),
                        ))
                        .await,
                    );
                }

                build_http_response_json(StatusCode::FORBIDDEN)
                    .await
                    .set_body(
                        to_json_string(&ResultMsg::<()>::fail_msg(Some(String::from(
                            error_util::USER_STATUS_UNAVAILABLE,
                        ))))
                        .await,
                    )
            }
            // 找不到直接返回相关错误提示
            Err(_) => {
                error!(
                    "登录失败: IP={}, 用户名={}, 密码={}",
                    req.connection_info().realip_remote_addr().unwrap(),
                    login_user.username,
                    login_user.password
                );

                build_http_response_json(StatusCode::NOT_FOUND)
                    .await
                    .set_body(
                        to_json_string(&ResultMsg::<()>::fail_msg(Some(String::from(
                            error_util::ERROR_USERNAME_OR_PASSWORD,
                        ))))
                        .await,
                    )
            }
        }
    } else {
        build_http_response_json(StatusCode::NOT_FOUND)
            .await
            .set_body(
                to_json_string(&ResultMsg::<()>::fail_msg(Some(String::from(
                    error_util::ERROR_CAPTCHA_CODE,
                ))))
                .await,
            )
    }
}

/**
 * 验证token是否有效
 */
#[post("/token")]
pub async fn sign_token(body: String) -> impl Responder {
    match sign_token_default::<Claims<TokenUser>>(body.as_str()).await {
        Ok(v) => {
            if v.claims.exp > Utc::now().timestamp_millis() as usize {
                return to_json_string(&ResultMsg::<()>::success_message(Some(String::from(
                    error_util::SUCCESS,
                ))))
                .await;
            }
        }
        Err(_) => {}
    }

    to_json_string(&ResultMsg::<()>::success_message(Some(String::from(
        error_util::NOT_REQUEST_ACCESS,
    ))))
    .await
}
