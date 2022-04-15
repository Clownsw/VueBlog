use super::tag::SelectTag;
use chrono::NaiveDateTime;
use serde::{Deserialize, Serialize};

/**
 * m_blog实体
 */
#[derive(Debug, Deserialize, Serialize)]
pub struct SelectBlog {
    pub id: i64,
    pub user_id: i64,
    pub title: String,
    pub description: String,
    pub content: String,
    pub created: NaiveDateTime,
    pub status: Option<i8>,
}

/**
 * 查询所有文章个数结构
 */
#[derive(Debug, Deserialize, Serialize)]
pub struct SelectCountBlog {
    pub count: i64,
}

/**
 * 请求博文实体
 */
#[derive(Debug, Deserialize, Serialize, Clone)]
pub struct RequestBlog {
    pub id: Option<i64>,
    pub user_id: i64,
    pub title: String,
    pub description: String,
    pub content: String,
    pub tag: Vec<SelectTag>,
}

/**
 * 添加博文实体
 */
#[derive(Debug, Deserialize, Serialize)]
pub struct InsertBlog {
    pub user_id: i64,
    pub title: String,
    pub description: String,
    pub content: String,
    pub created: NaiveDateTime,
    pub status: i8,
}

/**
 * 修改博文实体
 */
#[derive(Debug, Deserialize, Serialize)]
pub struct UpdateBlog {
    pub id: i64,
    pub title: String,
    pub description: String,
    pub content: String,
}
