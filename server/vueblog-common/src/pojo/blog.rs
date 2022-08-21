use chrono::NaiveDateTime;
use rustc_hash::FxHashMap;
use serde::{Deserialize, Serialize};
use sqlx::{mysql::MySqlRow, Row};

use super::{
    sort::SelectSortWithBlog,
    tag::{SelectBlogOther, SelectBlogTag},
};

///
/// m_blog实体
///
#[derive(Debug, Deserialize, Serialize)]
pub struct SelectBlog {
    pub id: i64,
    pub user_id: i64,
    pub title: String,
    pub description: String,
    pub content: String,
    pub created: NaiveDateTime,
    pub status: i8,
    pub sort_id: i32,
}

#[derive(Deserialize, Serialize)]
pub struct SelectBlogSortTag {
    pub id: i64,
    pub user_id: i64,
    pub sort_id: i32,
    pub title: String,
    pub content: String,
    pub description: String,
    pub created: NaiveDateTime,
    pub status: i8,
    pub sort: Option<SelectSortWithBlog>,
    pub tags: Option<Vec<SelectBlogOther>>,
}

impl SelectBlogSortTag {
    pub fn parse_map(row: &MySqlRow, map: &FxHashMap<&str, usize>) -> Self {
        SelectBlogSortTag {
            id: row.get(*(map.get("id").unwrap())),
            user_id: row.get(*(map.get("user_id").unwrap())),
            sort_id: row.get(*(map.get("sort_id").unwrap())),
            title: row.get(*(map.get("title").unwrap())),
            content: row.get(*(map.get("content").unwrap())),
            description: row.get(*(map.get("description").unwrap())),
            created: row.get(*(map.get("created").unwrap())),
            status: row.get(*(map.get("status").unwrap())),
            sort: None,
            tags: None,
        }
    }
}

#[derive(Deserialize, Serialize, Clone)]
pub struct SelectShowListBlog {
    pub id: i64,
    pub user_id: i64,
    pub sort_id: i32,
    pub title: String,
    pub description: String,
    pub created: NaiveDateTime,
    pub status: i8,
    pub sort: Option<SelectSortWithBlog>,
    pub tags: Option<Vec<SelectBlogOther>>,
}

impl SelectShowListBlog {
    pub fn parse_map(row: &MySqlRow, map: &FxHashMap<&str, usize>) -> Self {
        SelectShowListBlog {
            id: row.get(*(map.get("id").unwrap())),
            user_id: row.get(*(map.get("user_id").unwrap())),
            sort_id: row.get(*(map.get("sort_id").unwrap())),
            title: row.get(*(map.get("title").unwrap())),
            description: row.get(*(map.get("description").unwrap())),
            created: row.get(*(map.get("created").unwrap())),
            status: row.get(*(map.get("status").unwrap())),
            sort: None,
            tags: None,
        }
    }
}

///
/// 查询所有文章个数结构
///
#[derive(Default, Debug, Deserialize, Serialize)]
pub struct SelectCountBlog {
    pub count: i64,
}

///
/// 请求博文实体
///
#[derive(Debug, Deserialize, Serialize, Clone)]
pub struct RequestBlog {
    pub id: Option<i64>,
    pub user_id: i64,
    pub sort_id: i32,
    pub title: String,
    pub description: String,
    pub content: String,
    pub tag: Vec<SelectBlogTag>,
    pub status: i8,
    pub key: Option<String>,
    pub key_title: Option<String>,
}

///
/// 添加博文实体
///
#[derive(Debug, Deserialize, Serialize)]
pub struct InsertBlog {
    pub user_id: i64,
    pub sort_id: i32,
    pub title: String,
    pub description: String,
    pub content: String,
    pub created: NaiveDateTime,
    pub status: i8,
}

///
/// 修改博文实体
///
#[derive(Clone, Debug, Deserialize, Serialize)]
pub struct UpdateBlog {
    pub id: i64,
    pub sort_id: i32,
    pub title: String,
    pub description: String,
    pub content: String,
    pub status: i8,
}

#[derive(Clone, Debug, Deserialize, Serialize)]
pub struct SearchBlog {
    pub id: i64,
    pub title: String,
    pub description: String,
    pub content: String,
}

#[derive(Clone, Debug, Deserialize, Serialize)]
pub struct SearchBlogResult {
    pub id: i64,
    pub title: String,
    pub description: String,
}