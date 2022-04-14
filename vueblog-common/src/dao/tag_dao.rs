use sqlx::{mysql::MySqlQueryResult, MySqlPool};

use crate::pojo::tag::{InsertTag, SelectTag, Tag, UpdateTag};

/**
 * 查询所有标签
 */
pub async fn select_all(db_pool: &MySqlPool) -> Result<Vec<SelectTag>, sqlx::Error> {
    sqlx::query_as!(
        SelectTag,
        r#"
            SELECT * FROM m_tag
        "#
    )
    .fetch_all(db_pool)
    .await
}

/**
 * 添加一个标签
 */
pub async fn insert_tag(
    db_pool: &MySqlPool,
    insert_tag: InsertTag,
) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            INSERT INTO m_tag(name)
            VALUES(?)
        "#,
        insert_tag.name
    )
    .execute(db_pool)
    .await
}

/**
 * 更新一个标签
 */
pub async fn update_by_id(
    db_pool: &MySqlPool,
    update_tag: UpdateTag,
) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            UPDATE m_tag SET name = ? WHERE id = ?
        "#,
        update_tag.name,
        update_tag.id,
    )
    .execute(db_pool)
    .await
}

/**
 * 通过id删除标签
 */
pub async fn delete_by_id(db_pool: &MySqlPool, id: i64) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            DELETE FROM m_tag WHERE id = ?
        "#,
        id
    )
    .execute(db_pool)
    .await
}

/**
 * 查询博文的所有标签
 */
pub async fn select_all_by_blog_id(
    db_pool: &MySqlPool,
    blog_id: i64,
) -> Result<Vec<Tag>, sqlx::Error> {
    sqlx::query_as!(
        Tag,
        r#"
            SELECT
                m_tag.`name` 
            FROM
                m_tag
                RIGHT JOIN ( SELECT * FROM m_blogtag WHERE blogId = ? ) AS r ON m_tag.id = r.tagId
        "#,
        blog_id
    )
    .fetch_all(db_pool)
    .await
}
