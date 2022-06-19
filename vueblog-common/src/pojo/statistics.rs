use serde::{Deserialize, Serialize};

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct Statistics {
    /* 博文总条数 */
    pub blog_total_num: i64,
    /* 分类总个数 */
    pub sort_total_num: i64,
    /* 标签总个数 */
    pub tag_total_num: i64,
    /* 友链总个数 */
    pub friend_total_num: i64,
}

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct StatisticsBlog {
    pub id: i64,
    pub day: String,
    pub view_count: i64,
}
