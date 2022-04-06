use chrono::NaiveDateTime;
use serde::{Deserialize, Serialize};

/**
 * m_user实体
 */
#[derive(Debug, Clone, Deserialize, Serialize)]
pub struct SelectUser {
    pub id: i64,
    pub username: Option<String>,
    pub avatar: Option<String>,
    pub email: Option<String>,
    pub password: Option<String>,
    pub status: i32,
    pub created: NaiveDateTime,
    pub last_login: Option<NaiveDateTime>,
}

/**
 * 响应用户实体
 */
#[derive(Debug, Deserialize, Serialize)]
pub struct ResponseUser {
    pub token: String,
    pub id: i64,
    pub username: Option<String>,
    pub avatar: Option<String>,
    pub email: Option<String>,
    pub status: i32,
}

impl ResponseUser {
    pub fn from_select_user(token: String, select_user: SelectUser) -> Self {
        ResponseUser {
            token,
            id: select_user.id,
            username: select_user.username,
            avatar: select_user.avatar,
            email: select_user.email,
            status: select_user.status,
        }
    }
}

/**
 * 登录用户实体
 */
#[derive(Debug, Deserialize, Serialize)]
pub struct LoginUser {
    pub username: String,
    pub password: String,
    pub captcha_id: String,
    pub captcha_code: String,
}

impl LoginUser {
    pub fn new() -> Self {
        LoginUser {
            username: String::new(),
            password: String::new(),
            captcha_id: String::new(),
            captcha_code: String::new(),
        }
    }
}

/**
 * Token用户实体
 */
#[derive(Debug, PartialEq, Deserialize, Serialize)]
pub struct TokenUser {
    pub id: i64,
    pub username: String,
    pub status: i32,
}

impl TokenUser {
    pub fn from_select_user(select_user: SelectUser) -> Self {
        TokenUser {
            id: select_user.id,
            username: select_user.username.unwrap().clone(),
            status: select_user.status,
        }
    }
}

/**
 * 修改用户实体
 */
#[derive(Debug, Deserialize, Serialize)]
pub struct UpdateUser {
    pub id: i64,
    pub username: String,
    pub avatar: String,
    pub email: String,
    pub password: String,
    pub status: i32,
}
