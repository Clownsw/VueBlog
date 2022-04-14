use sqlx::MySqlPool;

use crate::pojo::tag::Tag;

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
