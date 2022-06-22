use redis::RedisError;
use redis_async_pool::{deadpool::managed::Object, RedisConnection};
use sqlx::{MySql, MySqlPool, Row};

use crate::{
    pojo::statistics::{InsertStatisticsBlog, Statistics, StatisticsBlog},
    util::{common_util::columns_to_map, redis_util},
};

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

pub async fn select_blog_statistics(
    db_pool: &MySqlPool,
) -> Result<Vec<StatisticsBlog>, sqlx::Error> {
    sqlx::query::<MySql>(
        r#"
            SELECT id, DATE_FORMAT(day, '%Y-%m-%d') AS day, view_count FROM m_blog_statistics
        "#,
    )
    .map(|row| {
        let columns = row.columns();
        let column_map = columns_to_map(columns);

        StatisticsBlog {
            id: row.get(*(column_map.get("id").unwrap())),
            day: row.get(*(column_map.get("day").unwrap())),
            view_count: row.get(*(column_map.get("view_count").unwrap())),
        }
    })
    .fetch_all(db_pool)
    .await
}

pub async fn save_blog_statistics(
    db_pool: &MySqlPool,
    insert_blog_statistics: InsertStatisticsBlog,
) -> Result<sqlx::mysql::MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            INSERT INTO m_blog_statistics(day, view_count) VALUES(?, ?)
        "#,
        insert_blog_statistics.day,
        insert_blog_statistics.view_count,
    )
    .execute(db_pool)
    .await
}

/**
 * 当日访问人数自增
 */
pub async fn blog_view_count_plus(conn: &mut Object<RedisConnection, RedisError>) {
    match redis_util::get::<String, &str>(conn, "blog_view_count").await {
        Ok(v) => {
            redis_util::set::<&str, i64>(conn, "blog_view_count", (v.parse::<i64>().unwrap()) + 1)
                .await;
        }
        Err(e) => {
            log::error!("{}", e);
        }
    }
}
