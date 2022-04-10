use sqlx::{mysql::MySqlQueryResult, MySqlPool};

use crate::pojo::system::{SelectSystem, UpdateSystem};

/**
 * 获取系统信息
 */
pub async fn select_system_info(db_pool: &MySqlPool) -> Result<SelectSystem, sqlx::Error> {
    sqlx::query_as!(
        SelectSystem,
        r#"
            SELECT * FROM m_system WHERE id = 1
        "#
    )
    .fetch_one(db_pool)
    .await
}

/**
 * 更新系统信息
 */
pub async fn update_system_info(
    db_pool: &MySqlPool,
    update_system: UpdateSystem,
) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            UPDATE m_system SET welcome = ?, title = ?, description = ? WHERE id = ?
        "#,
        update_system.welcome,
        update_system.title,
        update_system.description,
        update_system.id,
    )
    .execute(db_pool)
    .await
}
