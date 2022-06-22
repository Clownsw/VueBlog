use crate::pojo::user::{InsertUser, SelectUser, UpdateUser};
use crate::util::sql_util::build_what_sql_by_num;
use chrono::{NaiveDateTime, Utc};
use sqlx::mysql::MySqlQueryResult;
use sqlx::MySqlPool;

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

/**
 * 通过用户ID更新用户信息
 */
pub async fn update_by_id(
    db_pool: &MySqlPool,
    update_user: UpdateUser,
) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            UPDATE m_user SET username = ?, password = ?, avatar = ?, status = ? WHERE id = ?
        "#,
        update_user.username,
        update_user.password,
        update_user.avatar,
        update_user.status,
        update_user.id,
    )
    .execute(db_pool)
    .await
}

/**
 * 通过用户ID修改用户最后登陆时间
 */
pub async fn update_user_last_login_by_id(
    db_pool: &MySqlPool,
    id: i64,
    time: NaiveDateTime,
) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
        UPDATE m_user SET last_login = ? WHERE id = ?
        "#,
        time,
        id
    )
    .execute(db_pool)
    .await
}

/**
 * 添加一个用户
 */
pub async fn insert_user(
    db_pool: &MySqlPool,
    insert_user: InsertUser,
) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            INSERT INTO m_user(username, password, avatar, email, status, created, last_login)
            VALUES(?, ?, ?, ?, ?, ?, ?)
        "#,
        insert_user.username,
        insert_user.password,
        insert_user.avatar,
        insert_user.email,
        insert_user.status,
        Utc::now().naive_local(),
        Utc::now().naive_local(),
    )
    .execute(db_pool)
    .await
}

/**
 * 通过用户ID删除用户
 */
pub async fn delete_by_id(db_pool: &MySqlPool, id: i64) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            DELETE FROM m_user WHERE id = ?
        "#,
        id
    )
    .execute(db_pool)
    .await
}

/**
 * 通过一组用户ID删除用户
 */
pub async fn delete_by_ids(
    db_pool: &MySqlPool,
    ids: Vec<i64>,
) -> Result<MySqlQueryResult, sqlx::Error> {
    let query = format!("DELETE FROM m_user WHERE id IN ({})", {
        build_what_sql_by_num(ids.len()).await
    });

    let mut q = sqlx::query::<sqlx::MySql>(query.as_str());

    for id in ids {
        q = q.bind(id);
    }

    q.execute(db_pool).await
}
