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
#[derive(Debug, Deserialize, Serialize)]
pub struct RequestBlog {
    pub id: Option<i64>,
    pub user_id: i64,
    pub title: String,
    pub description: String,
    pub content: String,
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

/**
 * 分页博文向量
 */
#[derive(Debug, Deserialize, Serialize)]
pub struct LimitBlogs<T> {
    pub datas: Vec<T>,
    pub total: i64,   // 所有个数
    pub size: i64,    // 每页个数
    pub current: i64, // 当前页
    pub pages: i64,   // 总页数
}

impl<T> LimitBlogs<T> {
    pub fn from_unknown_datas(all_count: i64, current: i64, datas: Vec<T>) -> LimitBlogs<T> {
        let total: i64 = all_count;
        let size: i64 = 5;
        let mut pages: i64 = 0;

        {
            if total > 0 {
                if total % size == 0 {
                    pages = total / size;
                } else {
                    pages = (total / size) + 1;
                }
            }
        }

        LimitBlogs {
            datas,
            total,
            size,
            current,
            pages,
        }
    }
}
