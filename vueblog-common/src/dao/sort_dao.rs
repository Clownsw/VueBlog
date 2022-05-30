use sqlx::MySqlPool;

use crate::pojo::sort::{InsertSort, SelectSort, SelectSortWithBlog, UpdateSort};

/**
 * 查询所有分类
 */
pub async fn select_all(db_pool: &MySqlPool) -> Result<Vec<SelectSort>, sqlx::Error> {
    sqlx::query_as!(
        SelectSort,
        r#"
            SELECT * FROM m_sort
        "#
    )
    .fetch_all(db_pool)
    .await
}

/**
 * 添加一个分类
 */
pub async fn add_sort(
    db_pool: &MySqlPool,
    sort: InsertSort,
) -> Result<sqlx::mysql::MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            INSERT INTO m_sort(name) VALUES(?)
        "#,
        sort.name
    )
    .execute(db_pool)
    .await
}

/**
 * 通过ID更新分类
 */
pub async fn update_by_id(
    db_pool: &MySqlPool,
    update_sort: UpdateSort,
) -> Result<sqlx::mysql::MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            UPDATE m_sort SET name = ? WHERE id = ?
        "#,
        update_sort.name,
        update_sort.id
    )
    .execute(db_pool)
    .await
}

/**
 * 通过ID删除分类
 */
pub async fn delete_by_id(
    db_pool: &MySqlPool,
    id: i32,
) -> Result<sqlx::mysql::MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            DELETE FROM m_sort WHERE id = ?
        "#,
        id
    )
    .execute(db_pool)
    .await
}

/**
 * 通过博文ID查询分类
 */
pub async fn select_sort_by_blog_id(
    db_pool: &MySqlPool,
    blog_id: i64,
) -> Result<SelectSortWithBlog, sqlx::Error> {
    sqlx::query_as!(
        SelectSortWithBlog,
        r#"
            SELECT
                sort.* 
            FROM
                m_blog AS blog
                LEFT JOIN m_sort AS sort ON blog.sort_id = sort.id 
            WHERE
                blog.id = ?
        "#,
        blog_id
    )
    .fetch_one(db_pool)
    .await
}
