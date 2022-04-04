use sqlx::MySqlPool;

use crate::pojo::user::SelectUser;

/**
 * 查询所有用户
 */
pub async fn select_all_user(db_pool: &MySqlPool) -> Result<Vec<SelectUser>, sqlx::Error> {
    sqlx::query_as!(
        SelectUser,
        r#"
        SELECT * FROM m_user
    "#
    )
    .fetch_all(db_pool)
    .await
}

/**
 * 通过用户id查询用户
 */
pub async fn get_by_id(db_pool: &MySqlPool, user_id: i64) -> Result<SelectUser, sqlx::Error> {
    sqlx::query_as!(
        SelectUser,
        r#"
            SELECT * FROM m_user WHERE id = ?
        "#,
        user_id,
    )
    .fetch_one(db_pool)
    .await
}

/**
 * 通过用户名和密码查询用户
 */
pub async fn get_by_name_and_passwd(
    db_pool: &MySqlPool,
    username: &str,
    password: &str,
) -> Result<SelectUser, sqlx::Error> {
    sqlx::query_as!(
        SelectUser,
        r#"
            SELECT * FROM m_user WHERE username = ? AND password = ?
        "#,
        username,
        password
    )
    .fetch_one(db_pool)
    .await
}
