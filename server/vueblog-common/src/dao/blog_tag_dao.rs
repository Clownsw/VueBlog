use sqlx::{mysql::MySqlQueryResult, MySql, MySqlPool, Transaction};

use crate::{pojo::tag::SelectBlogTag, util::sql_util::build_what_sql_by_num};

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

/**
 * 批量删除包含指定标签id的博文的标签
 */
pub async fn delete_blog_tag_by_tag_ids(
    db_pool: &MySqlPool,
    blog_id: i64,
    tag_ids: Vec<i64>,
) -> Result<MySqlQueryResult, sqlx::Error> {
    let query = format!(
        "DELETE FROM m_blogtag WHERE blogId = {} AND tagId in ({})",
        blog_id,
        build_what_sql_by_num(tag_ids.len()).await
    );

    let mut q = sqlx::query::<MySql>(query.as_str());

    for item in tag_ids {
        q = q.bind(item);
    }

    q.execute(db_pool).await
}

/**
 * 批量删除包含指定标签id的博文的标签
 */
pub async fn delete_blog_tag_by_tag_ids_tran(
    tran: &mut Transaction<'_, MySql>,
    blog_id: i64,
    tag_ids: Vec<i64>,
) -> Result<MySqlQueryResult, sqlx::Error> {
    let query = format!(
        "DELETE FROM m_blogtag WHERE blogId = {} AND tagId in ({})",
        blog_id,
        build_what_sql_by_num(tag_ids.len()).await
    );

    let mut q = sqlx::query::<MySql>(query.as_str());

    for item in tag_ids {
        q = q.bind(item);
    }

    q.execute(tran).await
}

/**
 * 删除指定博文ID的所有标签
 */
pub async fn delete_all_by_blog_id(
    db_pool: &MySqlPool,
    id: i64,
) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            DELETE FROM m_blogtag WHERE blogId = ?
        "#,
        id
    )
    .execute(db_pool)
    .await
}

/**
 * 批量为指定博文添加标签
 */
pub async fn add_blog_tag_by_ids(
    db_pool: &MySqlPool,
    blog_id: i64,
    ids: Vec<i64>,
) -> Result<MySqlQueryResult, sqlx::Error> {
    let mut query = String::from(
        r#"
            INSERT INTO m_blogtag(blogId, tagId)
            VALUES
        "#,
    );

    for item in ids.clone() {
        query.push_str(format!("({}, {}),", blog_id, item).as_str());
    }
    query.remove(query.len() - 1);

    let mut q = sqlx::query::<MySql>(query.as_str());

    for item in ids {
        q = q.bind(item);
    }

    q.execute(db_pool).await
}

/**
 * 批量为指定博文添加标签
 */
pub async fn add_blog_tag_by_ids_tran(
    tran: &mut Transaction<'_, MySql>,
    blog_id: i64,
    tag: Vec<SelectBlogTag>,
) -> Result<MySqlQueryResult, sqlx::Error> {
    let mut query = String::from(
        r#"
            INSERT INTO m_blogtag(blogId, tagId, sort)
            VALUES
        "#,
    );

    for item in tag.clone() {
        query.push_str(format!("({}, {}, {}),", blog_id, item.id, item.sort).as_str());
    }
    query.remove(query.len() - 1);

    let mut q = sqlx::query::<MySql>(query.as_str());

    for item in tag {
        q = q.bind(item.id);
    }

    q.execute(tran).await
}

pub async fn update_batch_blog_tag_by_ids(
    tran: &mut Transaction<'_, MySql>,
    blog_id: i64,
    tag: Vec<SelectBlogTag>,
) -> Result<MySqlQueryResult, sqlx::Error> {
    let mut query = String::from(
        r#"
            UPDATE m_blogtag
            SET sort = CASE tagId
        "#,
    );

    for item in &tag {
        query.push_str(format!("WHEN {} THEN {} ", item.id, item.sort).as_str());
    }

    query.push_str(" END ");
    query.push_str(
        format!(
            "WHERE blogId = {} AND tagId IN ({})",
            blog_id,
            build_what_sql_by_num(tag.len()).await
        )
        .as_str(),
    );

    let mut q = sqlx::query::<MySql>(query.as_str());

    for item in tag {
        q = q.bind(item.id);
    }

    q.execute(tran).await
}
