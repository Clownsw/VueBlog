use job_scheduler::{Job, JobScheduler};
use redis_async_pool::RedisPool;
use sqlx::MySqlPool;

use crate::{
    config::global_config::GLOBAL_CONFIG, dao::staticstics_dao::save_blog_statistics,
    pojo::statistics::InsertStatisticsBlog,
};

use super::redis_util;

/**
 * 生成访问统计
 */
pub async fn build_view_count(db_pool: MySqlPool, redis_pool: RedisPool) {
    let mut sched = JobScheduler::new();

    let mut conn = redis_pool.get().await.unwrap();
    match redis_util::get::<i64, _>(&mut conn, "blog_view_count").await {
        Ok(_) => {}
        Err(_) => {
            redis_util::set::<&str, i64>(&mut conn, "blog_view_count", 0).await;
        }
    }
    // 每日凌晨 1点50分执行
    sched.add(Job::new(
        unsafe { GLOBAL_CONFIG.statisics_cron.parse().unwrap() },
        || {
            let db_pool_clone = (&db_pool).clone();
            let reds_pool_clone = (&redis_pool).clone();

            tokio::spawn(Box::pin(async move {
                let mut conn = reds_pool_clone.get().await.unwrap();
                let view_count = redis_util::get::<String, &str>(&mut conn, "blog_view_count")
                    .await
                    .unwrap()
                    .parse::<i64>()
                    .unwrap();

                if view_count != 0 {
                    // 生成前一天访问数
                    let insert_statistics_blog = InsertStatisticsBlog::new(
                        (chrono::Local::now() - chrono::Duration::days(1))
                            .format("%Y-%m-%d")
                            .to_string(),
                        view_count,
                    );

                    match save_blog_statistics(&db_pool_clone, insert_statistics_blog).await {
                        Err(e) => {
                            log::error!("{}", e);
                        }
                        _ => (),
                    }

                    // 重置
                    redis_util::set::<&str, i64>(&mut conn, "blog_view_count", 0).await;
                }
            }));
        },
    ));

    loop {
        sched.tick();

        tokio::time::sleep(tokio::time::Duration::from_millis(500)).await;
    }
}
