use chrono::Local;
use log::error;
use meilisearch_sdk::client::Client;
use meilisearch_sdk::indexes::Index;
use sqlx::{MySql, MySqlPool, Transaction};

use crate::{
    dao::{
        blog_dao::{add_blog_tran, get_by_id, update_blog_by_id, update_blog_key_by_id_tran},
        blog_tag_dao::{
            add_blog_tag_by_ids_tran, delete_blog_tag_by_tag_ids_tran, update_batch_blog_tag_by_ids,
        },
        other_dao::last_insert_id,
        tag_dao::{delete_blog_all_tag_by_blog_id, select_all_by_blog_id},
    },
    pojo::{
        blog::{InsertBlog, RequestBlog, UpdateBlog},
        key::UpdateBlogKey,
        tag::SelectBlogTag,
    },
    util::{common_util::get_del_and_add_and_default_vec, sql_util::sql_run_is_success},
};
use crate::dao::other_dao;
use crate::pojo::blog::SearchBlog;

pub async fn update_blog_key_by_id_tran_service(
    tran: &mut Transaction<'_, MySql>,
    id: i64,
    title: String,
    key: String,
) -> bool {
    sql_run_is_success(update_blog_key_by_id_tran(tran, UpdateBlogKey { id, title, key }).await)
        .await
}

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
                        ).await,
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
            }
        } else {
            if let Err(e) = delete_blog_all_tag_by_blog_id(&db_pool, request_blog.id.unwrap()).await
            {
                error!("{:#?}", e);
            }
        }
    }

    if request_blog.key != None || request_blog.key_title != None {
        if !update_blog_key_by_id_tran_service(
            &mut transactional,
            request_blog.id.clone().unwrap(),
            match request_blog.key_title.clone() {
                Some(v) => v,
                _ => String::new(),
            },
            match request_blog.key.clone() {
                Some(v) => v,
                _ => String::new(),
            },
        )
            .await
        {
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
        status: request_blog.status,
    };

    if sql_run_is_success(update_blog_by_id(&db_pool, update_blog).await).await {
        transactional.commit().await.unwrap();
        return true;
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
    blog_index: Index,
) -> bool {
    let mut transactional = db_pool.begin().await.unwrap();
    let request_blog_clone = request_blog.clone();

    let insert_blog = InsertBlog {
        user_id,
        sort_id: request_blog.sort_id,
        title: request_blog.title,
        description: request_blog.description,
        content: request_blog.content,
        created: Local::now().naive_local(),
        status: request_blog.status,
    };

    if sql_run_is_success(add_blog_tran(&mut transactional, insert_blog).await).await {
        let id = last_insert_id(&mut transactional).await.unwrap();
        let flag = request_blog.key != None && request_blog.key_title != None &&
            !request_blog.key.as_ref().unwrap().is_empty() &&
            !request_blog.key_title.as_ref().unwrap().is_empty();

        if flag {
            if !update_blog_key_by_id_tran_service(
                &mut transactional,
                id as i64,
                match request_blog.key_title.clone() {
                    Some(v) => v,
                    _ => String::new(),
                },
                match request_blog.key.clone() {
                    Some(v) => v,
                    _ => String::new(),
                },
            ).await
            {
                transactional.rollback().await.unwrap();
                return false;
            }
        }

        if request_blog.tag.len() > 0 {
            sql_run_is_success(
                add_blog_tag_by_ids_tran(&mut transactional, id as i64, request_blog.tag).await,
            )
                .await;
        }
        if !flag {
            let search_blog = SearchBlog {
                id: other_dao::last_insert_id(&mut transactional).await.unwrap() as i64,
                title: request_blog_clone.title,
                description: request_blog_clone.description,
                content: request_blog_clone.content,
            };

            blog_index.add_documents(&[search_blog], Some("id")).await.unwrap();
        }

        transactional.commit().await.unwrap();
        return true;
    }

    transactional.rollback().await.unwrap();
    false
}
