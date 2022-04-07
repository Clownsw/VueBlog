use super::{error_util, redis_util};
use crate::{
    config::global_config,
    pojo::{msg::ResultMsg, user::SelectUser},
};
use actix_web::{http::StatusCode, HttpResponse, HttpResponseBuilder};
use redis::RedisError;
use redis_async_pool::{deadpool::managed::Object, RedisConnection};
use serde::Serialize;
use std::{future::Future, pin::Pin};

/**
 * 创建一个响应对象_json
 */
pub async fn build_http_response_json(code: StatusCode) -> HttpResponse {
    HttpResponseBuilder::new(code)
        .content_type("application/json;charset=utf8")
        .finish()
}

/**
 * 验证验证码是否正确
 */
pub async fn sign_captcha_code(
    conn: &mut Object<RedisConnection, RedisError>,
    id: String,
    code: String,
) -> bool {
    let result = redis_util::get::<String, String>(conn, id).await;

    match result {
        Ok(v) => {
            if v.len() > global_config::CAPTCHA_CODE_NUM {
                return false;
            }

            return v == code;
        }
        Err(_) => {}
    }

    false
}

/**
 * 转换到JSON字符串
 */
pub async fn to_json_string<T: ?Sized + Serialize>(data: &T) -> String {
    serde_json::to_string(&data).unwrap()
}

pub async fn build_response_ok_all<T>(data: Option<T>, message: String) -> HttpResponse<String>
where
    T: Serialize,
{
    build_http_response_json(StatusCode::OK)
        .await
        .set_body(to_json_string(&ResultMsg::<T>::success_all(200, Some(message), data)).await)
}

/**
 * 返回一个200状态码的响应对象
 */
pub async fn build_response_ok_data<T>(data: T) -> HttpResponse<String>
where
    T: Serialize,
{
    build_response_ok_all(Some(data), String::from(error_util::SUCCESS)).await
}

/**
 * 返回一个200状态码的响应对象
 */
pub async fn build_response_ok_message(message: String) -> HttpResponse<String> {
    build_response_ok_all::<()>(None, message).await
}

/**
 * 返回一个200状态码的响应对象
 */
pub async fn build_response_ok_data_message<T>(data: T, message: String) -> HttpResponse<String>
where
    T: Serialize,
{
    build_response_ok_all(Some(data), message).await
}

pub async fn build_response_fail_all<T>(data: Option<T>, message: String) -> HttpResponse<String>
where
    T: Serialize,
{
    build_http_response_json(StatusCode::BAD_REQUEST)
        .await
        .set_body(to_json_string(&ResultMsg::<T>::fail_all(400, Some(message), data)).await)
}

/**
 * 返回一个400错误码的响应对象
 */
pub async fn build_response_baq_request_message(message: String) -> HttpResponse<String> {
    build_response_fail_all::<()>(None, message).await
}

/**
 * 返回一个400错误码响应对象
 */
pub async fn build_response_baq_request_data_message<T>(
    data: T,
    message: String,
) -> HttpResponse<String>
where
    T: Serialize,
{
    build_response_fail_all(Some(data), message).await
}

/**
 * 返回一个400错误码的响应对象
 */
pub async fn build_response_baq_request() -> HttpResponse<String> {
    build_response_baq_request_message(String::from(error_util::INCOMPLETE_REQUEST)).await
}

pub async fn test_aop<F, T, K>(f: F) -> T
where
    F: Fn(Option<K>) -> Pin<Box<dyn Future<Output = T>>>,
{
    f(None).await
}

pub async fn run_test_aop() {
    test_aop::<_, (), SelectUser>(|select_user| {
        Box::pin(async {
            match select_user {
                Some(v) => {
                    println!("v = {:?}", v);
                }
                None => {
                    println!("none")
                }
            };
        })
    })
    .await;
}
