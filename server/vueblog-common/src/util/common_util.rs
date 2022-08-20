use std::{fmt, future::Future, pin::Pin, process, str::FromStr};

use actix_web::{http::StatusCode, HttpRequest, HttpResponse, HttpResponseBuilder, Responder, web};
use chrono::Utc;
use meilisearch_sdk::search::SearchResult;
use redis::RedisError;
use redis_async_pool::{deadpool::managed::Object, RedisConnection};
use reqwest::Client;
use rustc_hash::FxHashMap;
use serde::Serialize;
use sqlx::{
    Column,
    mysql::{MySqlColumn, MySqlRow}, MySqlPool, Row,
};

use crate::{
    config::global_config::GLOBAL_CONFIG,
    pojo::{backup::SelectBackUp, msg::ResultMsg, status::AppState, user::SelectUser},
};

use super::{error_util, login_util::is_login_return, redis_util};

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
            if v.len() > GLOBAL_CONFIG.get().unwrap().captcha_code_num {
                return false;
            }

            return v == code;
        }
        Err(_) => {}
    }

    false
}

/**
 * 获取删除和新增向量
 */
pub async fn get_del_and_add_and_default_vec<T: fmt::Debug>(
    a: Vec<T>,
    b: Vec<T>,
) -> (Vec<T>, Vec<T>, Vec<T>)
    where
        T: PartialEq + Copy,
{
    let mut del: Vec<T> = vec![];
    let mut add: Vec<T> = vec![];
    let mut default: Vec<T> = vec![];

    // del
    for item_a in &a {
        if !b.contains(item_a) {
            del.push(*item_a);
        }
    }

    // add
    for item_b in &b {
        if !a.contains(item_b) {
            add.push(*item_b);
        }
    }

    // default
    for item in &a {
        if !del.contains(item) && !add.contains(item) && !default.contains(item) {
            default.push(*item);
        }
    }

    for item in &b {
        if !del.contains(item) && !add.contains(item) && !default.contains(item) {
            default.push(*item);
        }
    }

    (del, add, default)
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

/**
 * 登录拦截(AOP)
 *
 * 用户登录 → 登录成功 -> 继续
 *         ↓
 *         返回登录错误
 */
pub async fn security_interceptor_aop<F, T>(
    f: F,
    req: &HttpRequest,
    app_state: &web::Data<AppState>,
    body: Option<String>,
    data: Option<T>,
) -> impl Responder
    where
        F: Fn(
            &web::Data<AppState>,
            Option<String>,
            Option<T>,
            SelectUser,
        ) -> Pin<Box<dyn Future<Output=HttpResponse<String>>>>,
{
    // 验证用户是否登录
    let (user, error_msg) = is_login_return(req, &app_state.db_pool).await;
    if let Some(v) = error_msg {
        return build_response_baq_request_message(v).await;
    }

    f(app_state, body, data, user.unwrap()).await
}

pub fn columns_to_map(columns: &[MySqlColumn]) -> FxHashMap<&str, usize> {
    let mut map = FxHashMap::default();

    let mut i = 0;
    for item in columns {
        map.insert(item.name(), i);
        i += 1;
    }

    map
}

pub fn parse_sql_row_string<T, F: Fn(String) -> T>(
    row: &MySqlRow,
    map: &FxHashMap<&str, usize>,
    name: &str,
    f: F,
) -> Option<T> {
    match map.get(name) {
        Some(v) => {
            if let Some(v) = row.get::<Option<String>, _>(*v) {
                return Some(f(v));
            }
        }
        _ => {}
    }
    None
}

pub fn parse_string_to_parse_vec<T: FromStr>(s: String) -> Vec<T>
    where
        <T as FromStr>::Err: std::fmt::Debug,
{
    s.split(",")
        .collect::<Vec<&str>>()
        .iter()
        .map(|item| item.parse::<T>().unwrap())
        .collect::<Vec<T>>()
}

pub fn parse_string_to_string_vec(s: String) -> Vec<String> {
    s.split(",")
        .collect::<Vec<&str>>()
        .iter()
        .map(|item| String::from(*item))
        .collect::<Vec<String>>()
}

pub async fn like_table_async_run<
    T,
    E,
    F: Fn(T, &MySqlPool) -> Pin<Box<dyn Future<Output=T>>>,
>(
    f: F,
    result: Result<T, E>,
    db_pool: &MySqlPool,
) -> Result<T, E> {
    match result {
        Ok(v) => Ok(f(v, db_pool).await),
        Err(err) => Err(err),
    }
}

pub async fn dump_sql(backup: SelectBackUp) -> Result<String, anyhow::Error> {
    let output = process::Command::new("mysqldump")
        .arg(format!("-u{}", backup.username))
        .arg(format!("-p{}", backup.password))
        .arg("vueblog")
        .output()
        .unwrap();

    Ok(String::from_utf8(output.stdout)?)
}

pub async fn remote_upload_file(
    backup: SelectBackUp,
    file_content: String,
) -> Result<StatusCode, anyhow::Error> {
    let mut file_name = String::from("/sql_dump/dump_");
    file_name.push_str(Utc::now().to_string().as_str());
    file_name.push_str(".sql");

    let request_url = format!(
        "{}/{}/{}",
        "http://v0.api.upyun.com", backup.bucket_name, file_name
    );

    let resp = Client::new()
        .post(request_url.as_str())
        .basic_auth(backup.operator, Some(backup.operator_password))
        .body(file_content.as_bytes().to_vec())
        .send()
        .await?;

    Ok(resp.status())
}

pub async fn search_result_vec_to_vec<T: Clone>(search_result_vec: Vec<SearchResult<T>>) -> Vec<T> {
    search_result_vec.iter()
        .map(move |search_result| {
            search_result.result.clone()
        })
        .collect()
}

pub async fn test_aop<F, T, K>(f: F) -> T
    where
        F: Fn(Option<K>) -> Pin<Box<dyn Future<Output=T>>>,
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
