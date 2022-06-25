use chrono::Utc;
use sqlx::MySqlPool;

use crate::{
    dao::{
        blog_dao::{add_blog_tran, get_by_id, update_blog_by_id},
        blog_tag_dao::{
            add_blog_tag_by_ids_tran, delete_blog_tag_by_tag_ids_tran, update_batch_blog_tag_by_ids,
        },
        other_dao::last_insert_id,
        tag_dao::{delete_blog_all_tag_by_blog_id, select_all_by_blog_id},
    },
    pojo::{
        blog::{InsertBlog, RequestBlog, UpdateBlog},
        tag::SelectBlogTag,
    },
    util::{common_util::get_del_and_add_and_default_vec, sql_util::sql_run_is_success},
};

/**
 * 更新博客
 */
pub async fn blog_update_service(
    db_pool: &MySqlPool,
    request_blog: RequestBlog,
    blog_id: i64,
    new_tag_ids: Vec<i64>,
) -> bool {
    let mut transactional = db_pool.begin().await.unwrap();

    if let Ok(_) = get_by_id(db_pool, blog_id).await {
        if new_tag_ids.len() > 0 {
            if let Ok(old_tags) = select_all_by_blog_id(db_pool, blog_id).await {
                let old_tags_ids: Vec<i64> = old_tags.iter().map(|item| item.id.unwrap()).collect();

                let (dels, adds, default) =
                    get_del_and_add_and_default_vec(old_tags_ids, new_tag_ids).await;
                let mut result: Vec<bool> = vec![];

                if dels.len() > 0 {
                    result.push(
                        sql_run_is_success(
                            delete_blog_tag_by_tag_ids_tran(&mut transactional, blog_id, dels)
                                .await,
                        )
                        .await,
                    );
                }

                if adds.len() > 0 {
                    result.push(
                        sql_run_is_success(
                            add_blog_tag_by_ids_tran(
                                &mut transactional,
                                blog_id,
                                request_blog
                                    .tag
                                    .iter()
                                    .filter(|item| adds.contains(&item.id))
                                    .map(|item| item.clone())
                                    .collect::<Vec<SelectBlogTag>>(),
                            )
                            .await,
                        )
                        .await,
                    );
                }

                if default.len() > 0 {
                    result.push(
                        sql_run_is_success(
                            update_batch_blog_tag_by_ids(
                                &mut transactional,
                                request_blog.id.unwrap(),
                                request_blog
                                    .tag
                                    .iter()
                                    .filter(|item| default.contains(&item.id))
                                    .map(|item| item.clone())
                                    .collect::<Vec<SelectBlogTag>>(),
                            )
                            .await,
                        )
                        .await,
                    );
                }

                for r in result {
                    if !r {
                        transactional.rollback().await.unwrap();
                        return false;
                    }
                }

                let update_blog = UpdateBlog {
                    id: request_blog.id.unwrap(),
                    sort_id: request_blog.sort_id,
                    title: request_blog.title,
                    content: request_blog.content,
                    description: request_blog.description,
                };

                if sql_run_is_success(update_blog_by_id(&db_pool, update_blog).await).await {
                    transactional.commit().await.unwrap();
                    return true;
                }
            }
        } else {
            if sql_run_is_success(
                delete_blog_all_tag_by_blog_id(&db_pool, request_blog.id.unwrap()).await,
            )
            .await
            {
                transactional.commit().await.unwrap();
                return true;
            }
        }
    }

    transactional.rollback().await.unwrap();
    false
}

/**
 * 添加博客
 */
pub async fn blog_add_service(
    db_pool: &MySqlPool,
    user_id: i64,
    request_blog: RequestBlog,
) -> bool {
    let mut transactional = db_pool.begin().await.unwrap();

    let insert_blog = InsertBlog {
        user_id,
        sort_id: request_blog.sort_id,
        title: request_blog.title,
        description: request_blog.description,
        content: request_blog.content,
        created: Utc::now().naive_utc(),
        status: 0,
    };

    if sql_run_is_success(add_blog_tran(&mut transactional, insert_blog).await).await {
        let id = last_insert_id(&mut transactional).await.unwrap();

        if request_blog.tag.len() > 0 {
            sql_run_is_success(
                add_blog_tag_by_ids_tran(&mut transactional, id as i64, request_blog.tag).await,
            )
            .await;
        }

        transactional.commit().await.unwrap();
        return true;
    }

    transactional.rollback().await.unwrap();
    false
}
