use sqlx::{MySql, MySqlPool, Row};

pub async fn select_backup_info(db_pool: &MySqlPool) -> Result<String, sqlx::Error> {
    sqlx::query::<MySql>(
        r#"
            SELECT content FROM m_other WHERE `order` = 3
        "#,
    )
    .map(|row| {
        let content: String = row.get(0);
        content
    })
    .fetch_one(db_pool)
    .await
}

pub async fn update_backup_info_content(
    db_pool: &MySqlPool,
    content: String,
) -> Result<sqlx::mysql::MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            UPDATE m_other SET content = ? WHERE `order` = 3
        "#,
        content
    )
    .execute(db_pool)
    .await
}
