use sqlx::MySqlPool;

use crate::pojo::statistics::Statistics;

use super::{blog_dao, friend_dao, sort_dao, tag_dao};

/**
 * 查询统计数据
 */
pub async fn select_statistics(db_pool: &MySqlPool) -> Result<Statistics, sqlx::Error> {
    /* 查询博文条数 */
    let blog_total_num = blog_dao::select_all_count(db_pool).await?.count;

    /* 查询分类个数 */
    let sort_total_num = sort_dao::select_all_count(db_pool).await?.count;

    /* 查询标签个数 */
    let tag_total_num = tag_dao::select_all_count(db_pool).await?.count;

    /* 查询友链个数 */
    let friend_total_num = friend_dao::select_all_count(db_pool).await?.count;

    Ok(Statistics {
        blog_total_num,
        sort_total_num,
        tag_total_num,
        friend_total_num,
    })
}
