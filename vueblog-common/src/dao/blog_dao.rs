use crate::{
    pojo::blog::{InsertBlog, SelectBlog, SelectCountBlog, UpdateBlog},
    util::sql_util::build_what_sql_by_num,
};
use sqlx::mysql::{MySqlPool, MySqlQueryResult};

/**
 * 查询所有文章个数
 */
pub async fn select_all_count(db_pool: &MySqlPool) -> Result<Vec<SelectCountBlog>, sqlx::Error> {
    sqlx::query_as!(
        SelectCountBlog,
        r#"SELECT COUNT(*) as count FROM m_blog WHERE id != 1"#
    )
    .fetch_all(db_pool)
    .await
}

/**
 * 查找所有文章
 */
pub async fn select_all(db_pool: &MySqlPool) -> Result<Vec<SelectBlog>, sqlx::Error> {
    sqlx::query_as!(
        SelectBlog,
        r#"
            SELECT * FROM m_blog WHERE id != 1
        "#
    )
    .fetch_all(db_pool)
    .await
}

/**
 * 分页查询文章
 */
pub async fn select_all_limit(
    db_pool: &MySqlPool,
    limit: i64,
    size: i64,
) -> Result<Vec<SelectBlog>, sqlx::Error> {
    sqlx::query_as!(
        SelectBlog,
        r#"
            SELECT * FROM m_blog WHERE id != 1 ORDER BY created DESC LIMIT ?, ?
        "#,
        limit,
        size
    )
    .fetch_all(db_pool)
    .await
}

/**
 * 通过id查询文章
 */
pub async fn get_by_id(db_pool: &MySqlPool, blog_id: i64) -> Result<SelectBlog, sqlx::Error> {
    sqlx::query_as!(
        SelectBlog,
        r#"
            SELECT * FROM m_blog WHERE id = ?
        "#,
        blog_id
    )
    .fetch_one(db_pool)
    .await
}

/**
 * 通过名称查询文章
 */
pub async fn select_by_title(
    db_pool: &MySqlPool,
    title: String,
) -> Result<SelectBlog, sqlx::Error> {
    sqlx::query_as!(
        SelectBlog,
        r#"
            SELECT * FROM m_blog WHERE title = ?
        "#,
        title
    )
    .fetch_one(db_pool)
    .await
}

/**
 * 添加一个博文
 */
pub async fn add_blog(
    db_pool: &MySqlPool,
    insert_blog: InsertBlog,
) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            INSERT INTO m_blog(user_id, title, description, content, created, status)
            VALUES(?, ?, ?, ?, ?, ?)
        "#,
        insert_blog.user_id,
        insert_blog.title,
        insert_blog.description,
        insert_blog.content,
        insert_blog.created,
        insert_blog.status
    )
    .execute(db_pool)
    .await
}

/**
 * 修改博文
 */
pub async fn update_blog_by_id(
    db_pool: &MySqlPool,
    update_blog: UpdateBlog,
) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            UPDATE m_blog SET title = ?, description = ?, content = ? WHERE id = ?
        "#,
        update_blog.title,
        update_blog.description,
        update_blog.content,
        update_blog.id
    )
    .execute(db_pool)
    .await
}

/**
 * 通过ID批量删除博文
 */
pub async fn delete_by_ids(
    db_pool: &MySqlPool,
    ids: Vec<i64>,
) -> Result<MySqlQueryResult, sqlx::Error> {
    let query = format!(
        "DELETE FROM m_blog WHERE id in({})",
        build_what_sql_by_num(ids.len()).await
    );

    let mut q = sqlx::query::<sqlx::MySql>(query.as_str());

    for id in ids {
        q = q.bind(id);
    }

    q.execute(db_pool).await
}
