use std::{future::Future, pin::Pin};

use actix_web::{http::StatusCode, HttpResponse, HttpResponseBuilder};
use redis::{AsyncCommands, RedisError};
use redis_async_pool::{deadpool::managed::Object, RedisConnection};

use crate::{config::global_config, pojo::user::SelectUser};

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
    let result = conn.get::<_, String>(id).await;

    match result {
        Ok(v) => {
            if v.len() > global_config::CAPTCHA_CODE_NUM {
                return false;
            }

            return v == code.to_lowercase();
        }
        Err(_) => {}
    }

    false
}

pub async fn test_aop<F, T>(f: F) -> T
where
    F: Fn(Option<SelectUser>) -> Pin<Box<dyn Future<Output = T>>>,
{
    f(None).await
}

pub async fn run_test_aop() {
    test_aop(|select_user| {
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
