use sqlx::MySqlPool;

/**
 * 删除所有包含指定标签id的博文的标签
 */
pub async fn delete_all_blog_by_tag_id(
    db_pool: &MySqlPool,
    tag_id: i64,
) -> Result<sqlx::mysql::MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            DELETE FROM m_blogtag WHERE tagId = ?
        "#,
        tag_id
    )
    .execute(db_pool)
    .await
}
