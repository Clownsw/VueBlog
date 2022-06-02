use sqlx::MySqlPool;

use crate::pojo::me::{SelectMe, UpdateMe};

/**
 * 查找指定用户的关于我页面
 */
pub async fn select_me_by_user_id(db_pool: &MySqlPool) -> Result<SelectMe, sqlx::Error> {
    sqlx::query_as! {
        SelectMe,
        r#"
            SELECT * FROM m_other
        "#
    }
    .fetch_one(db_pool)
    .await
}

/**
 * 通过ID更新关于我
 */
pub async fn update_me_by_id(
    db_pool: &MySqlPool,
    update_me: UpdateMe,
) -> Result<sqlx::mysql::MySqlQueryResult, sqlx::Error> {
    sqlx::query! {
        r#"
            UPDATE m_other SET title = ?, content = ? WHERE id = ?
        "#,
        update_me.title,
        update_me.content,
        update_me.id
    }
    .execute(db_pool)
    .await
}
