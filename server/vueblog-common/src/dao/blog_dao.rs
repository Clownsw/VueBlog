use sqlx::{
    mysql::{MySqlPool, MySqlQueryResult},
    MySql, Row, Transaction,
};

use crate::{
    pojo::{
        blog::{InsertBlog, SelectBlog, SelectBlogSortTag, SelectCountBlog, SelectShowListBlog, UpdateBlog},
        key::{SelectBlogKey, UpdateBlogKey},
        other::SelectCount, sort::SelectSortWithBlog, tag::SelectBlogOther,
    },
    util::{
        common_util::{columns_to_map, parse_sql_row_string, parse_string_to_parse_vec, parse_string_to_string_vec},
        sql_util::build_what_sql_by_num,
    },
};

/**
 * 查询所有文章个数
 */
pub async fn select_all_count<'a>(db_pool: &MySqlPool, query_str: Option<&'a str>) -> Result<SelectCount, sqlx::Error> {
    let mut sql = String::from("SELECT COUNT(1) as count FROM m_blog WHERE status = 0");

    if let Some(v) = query_str {
        sql.push_str(format!(" AND MATCH(m_blog.title, m_blog.description, m_blog.content) AGAINST('{}')", v).as_str());
    }

    sqlx::query_as::<_, SelectCount>(sql.as_str())
        .fetch_one(db_pool)
        .await
}

/**
 * 查询指定分类下的所有文章个数
 */
pub async fn select_sort_all_count(db_pool: &MySqlPool, sort_id: i32) -> Result<SelectCountBlog, sqlx::Error> {
    sqlx::query_as!(
        SelectCountBlog,
        r#"
            SELECT COUNT(1) as count FROM m_blog WHERE sort_id = ?
        "#,
        sort_id
    )
        .fetch_one(db_pool)
        .await
}

/**
 * 查询指定标签下的所有文章个数
 */
pub async fn select_tag_all_count(db_pool: &MySqlPool, tag_id: i64) -> Result<SelectCountBlog, sqlx::Error> {
    sqlx::query_as!(
        SelectCountBlog,
        r#"
            SELECT
                COUNT( DISTINCT ( mbt.blogId ) ) AS count 
            FROM
                m_blogtag AS mbt
                LEFT JOIN m_blog mb ON mb.id = mbt.blogId 
            WHERE
                mbt.tagId = ? 
                AND mb.`status` = 0
        "#,
        tag_id
    )
        .fetch_one(db_pool)
        .await
}

/**
 * 查找所有文章
 */
pub async fn select_all(db_pool: &MySqlPool) -> Result<Vec<SelectBlog>, sqlx::Error> {
    sqlx::query_as!(
        SelectBlog,
        r#"
            SELECT * FROM m_blog
        "#
    )
        .fetch_all(db_pool)
        .await
}

/**
 * 分页查询文章
 */
pub async fn select_all_limit<'a>(
    db_pool: &MySqlPool,
    limit: i64,
    size: i64,
    is_login: bool,
) -> Result<Vec<SelectShowListBlog>, sqlx::Error> {
    let status_query_str = match is_login {
        true => "",
        false => r#"
            WHERE
                blog.status = 0
        "#
    };

    sqlx::query::<MySql>(
        format!(r#"
        SELECT
            blog.id,
            blog.user_id,
            blog.sort_id,
            blog.title,
            blog.description,
            blog.created,
            blog.status AS status,
            sort.NAME AS sort_name,
            sort.order AS sort_order,
            (
                SELECT
                    GROUP_CONCAT( `id` ORDER BY mbt.sort )
                FROM
                    m_tag AS tag
                    RIGHT JOIN ( SELECT * FROM m_blogtag WHERE blogId = blog.id ) AS mbt ON tag.id IN ( mbt.tagId )
            ) AS 'tag_ids',
            (
                SELECT
                    GROUP_CONCAT( `name` ORDER BY mbt.sort )
                FROM
                    m_tag AS tag
                    RIGHT JOIN ( SELECT * FROM m_blogtag WHERE blogId = blog.id ) AS mbt ON tag.id IN ( mbt.tagId )
            ) AS 'tag_names'
            FROM
                m_blog AS blog
                LEFT JOIN m_sort AS sort ON blog.sort_id = sort.id

            {}

            ORDER BY
                created DESC
                LIMIT ?, ?
    "#, status_query_str).as_str(),
    )
        .bind(limit)
        .bind(size)
        .map(|row| {
            let columns = row.columns();
            let map = columns_to_map(columns);
            let mut blog = SelectShowListBlog::parse_map(&row, &map);

            blog.sort = Some(SelectSortWithBlog {
                id: row.get(*(map.get("sort_id")).unwrap()),
                order: row.get(*(map.get("sort_order")).unwrap()),
                name: row.get(*(map.get("sort_name")).unwrap()),
            });

            let ids = if let Some(v) = parse_sql_row_string(&row, &map, "tag_ids", parse_string_to_parse_vec) {
                v
            } else {
                Vec::new()
            };

            let names = if let Some(v) = parse_sql_row_string(&row, &map, "tag_names", parse_string_to_string_vec) {
                v
            } else {
                Vec::new()
            };

            let mut tags = Vec::<SelectBlogOther>::new();
            let len = ids.len();
            for i in 0..len {
                tags.push(SelectBlogOther {
                    id: Some(*(ids.get(i).unwrap())),
                    name: Some(names.get(i).unwrap().clone()),
                })
            }

            blog.tags = Some(tags);

            blog
        })
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
 * 通过ID和key查询文章
 */
pub async fn get_by_id_and_key(db_pool: &MySqlPool, blog_id: i64, key: &str) -> Result<SelectBlog, sqlx::Error> {
    sqlx::query_as!(
        SelectBlog,
        r#"
            SELECT 
                blog.*
            FROM 
                m_blog AS blog
            LEFT JOIN m_blog_key AS mbk ON blog.id = mbk.id  
            WHERE 
                blog.id = ? AND mbk.`key` = ?
        "#,
        blog_id,
        key
    )
        .fetch_one(db_pool)
        .await
}

/**
 * 通过ID获取指定文章秘钥
 */
pub async fn get_blog_key_by_id(db_pool: &MySqlPool, blog_id: i64) -> Result<SelectBlogKey, sqlx::Error> {
    sqlx::query_as!(
        SelectBlogKey,
        r#"
            SELECT title, `key` FROM m_blog_key WHERE id = ?
        "#,
        blog_id
    )
        .fetch_one(db_pool)
        .await
}

pub async fn get_by_id_with_sort_and_tag(
    db_pool: &MySqlPool,
    blog_id: i64,
) -> Result<SelectBlogSortTag, sqlx::Error> {
    sqlx::query::<MySql>(
        r#"
                SELECT
                blog.id,
                blog.user_id,
                blog.sort_id,
                IF(blog.status = 1, blog_key.title, blog.title) AS title,
                blog.content,
                IF(blog.status = 1, blog_key.title, blog.description) AS description,
                blog.created,
                blog.STATUS AS status,
                sort.NAME AS sort_name,
                sort.ORDER AS sort_order,
                (
                SELECT
                    GROUP_CONCAT( `id` ORDER BY mbt.sort ) 
                FROM
                    m_tag AS tag
                    RIGHT JOIN ( SELECT * FROM m_blogtag WHERE blogId = blog.id ) AS mbt ON tag.id IN ( mbt.tagId ) 
                ) AS 'tag_ids',
                (
                SELECT
                    GROUP_CONCAT( `name` ORDER BY mbt.sort ) 
                FROM
                    m_tag AS tag
                    RIGHT JOIN ( SELECT * FROM m_blogtag WHERE blogId = blog.id ) AS mbt ON tag.id IN ( mbt.tagId ) 
                ) AS 'tag_names' 
                FROM
                    m_blog AS blog
                    LEFT JOIN m_sort AS sort ON blog.sort_id = sort.id 
                    LEFT JOIN m_blog_key AS blog_key ON blog_key.id = blog.id
                WHERE
                    blog.id = ?
            "#
    )
        .bind(blog_id)
        .map(|row| {
            let columns = row.columns();
            let map = columns_to_map(columns);
            let mut blog = SelectBlogSortTag::parse_map(&row, &map);

            blog.sort = Some(SelectSortWithBlog {
                id: row.get(*(map.get("sort_id")).unwrap()),
                order: row.get(*(map.get("sort_order")).unwrap()),
                name: row.get(*(map.get("sort_name")).unwrap()),
            });

            let ids = if let Some(v) = parse_sql_row_string(&row, &map, "tag_ids", parse_string_to_parse_vec) {
                v
            } else {
                Vec::new()
            };

            let names = if let Some(v) = parse_sql_row_string(&row, &map, "tag_names", parse_string_to_string_vec) {
                v
            } else {
                Vec::new()
            };

            let mut tags = Vec::<SelectBlogOther>::new();
            let len = ids.len();
            for i in 0..len {
                tags.push(SelectBlogOther {
                    id: Some(*(ids.get(i).unwrap())),
                    name: Some(names.get(i).unwrap().clone()),
                })
            }

            blog.tags = Some(tags);
            blog
        })
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
            INSERT INTO m_blog(user_id, sort_id, title, description, content, created, status)
            VALUES(?, ?, ?, ?, ?, ?, ?)
        "#,
        insert_blog.user_id,
        insert_blog.sort_id,
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
 * 添加一个博文
 */
pub async fn add_blog_tran(
    tran: &mut Transaction<'_, MySql>,
    insert_blog: InsertBlog,
) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            INSERT INTO m_blog(user_id, sort_id, title, description, content, created, status)
            VALUES(?, ?, ?, ?, ?, ?, ?)
        "#,
        insert_blog.user_id,
        insert_blog.sort_id,
        insert_blog.title,
        insert_blog.description,
        insert_blog.content,
        insert_blog.created,
        insert_blog.status
    )
        .execute(tran)
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
            UPDATE m_blog SET title = ?, description = ?, content = ?, sort_id = ?, status = ? WHERE id = ?
        "#,
        update_blog.title,
        update_blog.description,
        update_blog.content,
        update_blog.sort_id,
        update_blog.status,
        update_blog.id
    )
        .execute(db_pool)
        .await
}

/**
 * 更新博文秘钥
 */
pub async fn update_blog_key_by_id_tran(tran: &mut Transaction<'_, MySql>, update_blog_key: UpdateBlogKey) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            REPLACE INTO m_blog_key(id, title, `key`) VALUES(?, ?, ?)
        "#,
        update_blog_key.id,
        update_blog_key.title,
        update_blog_key.key
    )
        .execute(tran)
        .await
}

/**
 * 通过ID批量删除博文
 */
pub async fn delete_by_ids_tran(
    tran: &mut Transaction<'_, MySql>,
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

    q.execute(tran).await
}

pub async fn delete_key_by_ids_tran(tran: &mut Transaction<'_, MySql>, ids: Vec<i64>) -> Result<MySqlQueryResult, sqlx::Error> {
    let query = format!(
        "DELETE FROM m_blog_key WHERE id in({})",
        build_what_sql_by_num(ids.len()).await
    );

    let mut q = sqlx::query::<sqlx::MySql>(query.as_str());

    for id in ids {
        q = q.bind(id);
    }

    q.execute(tran).await
}

/**
 * 更新指定分类下的所有文章分类到新的分类
 */
pub async fn update_blog_sort_to_new_by_sort_id(db_pool: &MySqlPool, sort_id: i32, new_sort_id: i32) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            UPDATE m_blog SET sort_id = ? WHERE sort_id = ?
        "#,
        new_sort_id,
        sort_id,
    )
        .execute(db_pool)
        .await
}

/**
 * 分页查询指定分类下的所有博文
 */
pub async fn select_all_limit_by_sort_id(db_pool: &MySqlPool, limit: i64, size: i64, sort_id: i32) -> Result<Vec<SelectShowListBlog>, sqlx::Error> {
    sqlx::query::<MySql>(
        r#"
                SELECT
                blog.id,
                blog.user_id,
                blog.sort_id,
                IF(blog.status = 1, blog_key.title, blog.title) AS title,
                IF(blog.status = 1, blog_key.title, blog.description) AS description,
                blog.created,
                blog.status AS status,
                sort.NAME AS sort_name,
                sort.order AS sort_order,
                (
                    SELECT
                        GROUP_CONCAT( `id` ORDER BY mbt.sort ) 
                    FROM
                        m_tag AS tag
                        RIGHT JOIN ( SELECT * FROM m_blogtag WHERE blogId = blog.id ) AS mbt ON tag.id IN ( mbt.tagId ) 
                ) AS 'tag_ids',
                (
                    SELECT
                        GROUP_CONCAT( `name` ORDER BY mbt.sort ) 
                    FROM
                        m_tag AS tag
                        RIGHT JOIN ( SELECT * FROM m_blogtag WHERE blogId = blog.id ) AS mbt ON tag.id IN ( mbt.tagId ) 
                ) AS 'tag_names'
                FROM
                    m_blog AS blog
                    LEFT JOIN m_sort AS sort ON blog.sort_id = sort.id
                    LEFT JOIN m_blog_key AS blog_key ON blog_key.id = blog.id
                WHERE
                    blog.sort_id = ?
                ORDER BY
                    created DESC 
                    LIMIT ?, ?
            "#,
    )
        .bind(sort_id)
        .bind(limit)
        .bind(size)
        .map(|row| {
            let columns = row.columns();
            let map = columns_to_map(columns);
            let mut blog = SelectShowListBlog::parse_map(&row, &map);

            blog.sort = Some(SelectSortWithBlog {
                id: row.get(*(map.get("sort_id")).unwrap()),
                order: row.get(*(map.get("sort_order")).unwrap()),
                name: row.get(*(map.get("sort_name")).unwrap()),
            });

            let ids = if let Some(v) = parse_sql_row_string(&row, &map, "tag_ids", parse_string_to_parse_vec) {
                v
            } else {
                Vec::new()
            };

            let names = if let Some(v) = parse_sql_row_string(&row, &map, "tag_names", parse_string_to_string_vec) {
                v
            } else {
                Vec::new()
            };

            let mut tags = Vec::<SelectBlogOther>::new();
            let len = ids.len();
            for i in 0..len {
                tags.push(SelectBlogOther {
                    id: Some(*(ids.get(i).unwrap())),
                    name: Some(names.get(i).unwrap().clone()),
                })
            }

            blog.tags = Some(tags);

            blog
        })
        .fetch_all(db_pool)
        .await
}

/**
 * 分页查询指定标签下的所有博文
 */
pub async fn select_all_limit_by_tag_id(db_pool: &MySqlPool, limit: i64, size: i64, tag_id: i64) -> Result<Vec<SelectShowListBlog>, sqlx::Error> {
    sqlx::query::<MySql>(
        r#"
                SELECT
                    blog.id,
                    blog.user_id,
                    blog.sort_id,
                    blog.title,
                    blog.description,
                    blog.created,
                    blog.status AS status,
                    sort.NAME AS sort_name,
                    sort.order AS sort_order,
                    (
                        SELECT
                            GROUP_CONCAT( `id` ORDER BY mbt.sort ) 
                        FROM
                            m_tag AS tag
                            RIGHT JOIN ( SELECT * FROM m_blogtag WHERE blogId = blog.id ) AS mbt ON tag.id IN ( mbt.tagId ) 
                    ) AS 'tag_ids',
                    (
                        SELECT
                            GROUP_CONCAT( `name` ORDER BY mbt.sort ) 
                        FROM
                            m_tag AS tag
                            RIGHT JOIN ( SELECT * FROM m_blogtag WHERE blogId = blog.id ) AS mbt ON tag.id IN ( mbt.tagId ) 
                    ) AS 'tag_names'
                    FROM
                        m_blog AS blog
                        LEFT JOIN m_sort AS sort ON blog.sort_id = sort.id
                    WHERE
                        blog.id IN (
                            SELECT DISTINCT
                                blogId
                            FROM
                                m_blogtag
                            WHERE tagId = ?
                        )
                        AND
                        blog.status = 0

                    ORDER BY
                        created DESC 
                        LIMIT ?, ?
                "#,
    )
        .bind(tag_id)
        .bind(limit)
        .bind(size)
        .map(|row| {
            let columns = row.columns();
            let map = columns_to_map(columns);
            let mut blog = SelectShowListBlog::parse_map(&row, &map);

            blog.sort = Some(SelectSortWithBlog {
                id: row.get(*(map.get("sort_id")).unwrap()),
                order: row.get(*(map.get("sort_order")).unwrap()),
                name: row.get(*(map.get("sort_name")).unwrap()),
            });

            let ids = if let Some(v) = parse_sql_row_string(&row, &map, "tag_ids", parse_string_to_parse_vec) {
                v
            } else {
                Vec::new()
            };

            let names = if let Some(v) = parse_sql_row_string(&row, &map, "tag_names", parse_string_to_string_vec) {
                v
            } else {
                Vec::new()
            };

            let mut tags = Vec::<SelectBlogOther>::new();
            let len = ids.len();
            for i in 0..len {
                tags.push(SelectBlogOther {
                    id: Some(*(ids.get(i).unwrap())),
                    name: Some(names.get(i).unwrap().clone()),
                })
            }

            blog.tags = Some(tags);

            blog
        })
        .fetch_all(db_pool)
        .await
}