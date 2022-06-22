use sqlx::{MySql, MySqlPool, Row, Transaction};

use crate::pojo::{
    me::{SelectMe, UpdateMe},
    other::{SelectPageFooter, UpdatePageFooter},
};

/**
 * 查找关于我
 */
pub async fn select_me_by_user_id(db_pool: &MySqlPool) -> Result<SelectMe, sqlx::Error> {
    sqlx::query_as! {
        SelectMe,
        r#"
            SELECT id, title, content FROM m_other WHERE `order` = 0
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
            UPDATE m_other SET title = ?, content = ? WHERE id = ? AND `order` = 0
        "#,
        update_me.title,
        update_me.content,
        update_me.id
    }
    .execute(db_pool)
    .await
}

/**
 * 查询网页底部
 */
pub async fn select_page_footer(db_pool: &MySqlPool) -> Result<SelectPageFooter, sqlx::Error> {
    sqlx::query_as!(
        SelectPageFooter,
        r#"
            SELECT content FROM m_other WHERE `order` = 1
        "#
    )
    .fetch_one(db_pool)
    .await
}

/**
 * 更新网页底部
 */
pub async fn update_page_footer(
    db_pool: &MySqlPool,
    update_footer: UpdatePageFooter,
) -> Result<sqlx::mysql::MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            UPDATE m_other SET content = ? WHERE `order` = 1
        "#,
        update_footer.content
    )
    .execute(db_pool)
    .await
}

pub async fn last_insert_id(tran: &mut Transaction<'_, MySql>) -> Result<u64, sqlx::Error> {
    sqlx::query::<MySql>(
        r#"
            SELECT LAST_INSERT_ID();
        "#,
    )
    .map(|row| {
        let id: u64 = row.get(0);
        id
    })
    .fetch_one(tran)
    .await
}
