use actix_cors::Cors;
use actix_web::{guard, web, App, HttpServer};
use log::info;
use redis_async_pool::{RedisConnectionManager, RedisPool};
use sqlx::{MySqlPool, Pool};

use vueblog_common::{
    config::global_config,
    controller::{
        blog_controller::{blog_deletes, blog_detail, blog_edit, blog_list},
        default_controller::not_found_page,
        friend_controller::{friend_add, friend_all, friend_deletes, friend_limit, friend_update},
        login_controller::{login, sign_token},
        me_controller::{me, me_update},
        other_controller::generate_captcha_code,
        sort_controlller::{sort_add, sort_list, sort_remove, sort_update},
        system_controller::{system_info, system_update},
        tag_controller::{
            tag_add, tag_all, tag_delete, tag_id_by_name, tag_is_exist, tag_update, tags_blog,
        },
        user_controller::{all_user, user_add, user_deletes, user_info, user_update},
    },
    pojo::status::AppState,
};

/**
 * 初始化数据库连接池
 */
async fn make_db_pool() -> MySqlPool {
    let sql_url = std::env::var("DATABASE_URL").unwrap();
    Pool::connect(&sql_url).await.unwrap()
}

/**
 * 初始化redis客户端
 */
async fn make_redis_client() -> RedisPool {
    let redis_url = std::env::var("REDIS_URL").unwrap();
    let redis_pool_num = std::env::var("REDIS_POOL_NUM")
        .unwrap()
        .parse::<usize>()
        .unwrap();

    RedisPool::new(
        RedisConnectionManager::new(redis::Client::open(redis_url).unwrap(), true, None),
        redis_pool_num,
    )
}

/**
 * 初始化JWT_KEY
 */
async fn make_jwt_key() {
    let jwt_key = std::env::var("JWT_KEY").unwrap();
    let mut key = global_config::KEY.lock().unwrap();
    *key = jwt_key;
}

/**
 * 初始化
 */
async fn init() -> (String, u16, usize, MySqlPool, RedisPool) {
    // 加载.env
    dotenv::dotenv().ok();

    // 初始化日志
    pretty_env_logger::init_custom_env("VUEBLOG_AFTER_LOG_LEVEL");

    // HTTP worker 个数
    let workers = std::env::var("VUEBLOG_AFTER_WORKERS")
        .unwrap()
        .parse::<usize>()
        .unwrap();

    // 数据库连接池
    let db_pool = make_db_pool().await;

    // redis客户端
    let redis_client = make_redis_client().await;

    // JWT_KEY
    make_jwt_key().await;

    // 服务地址
    let server_address = std::env::var("VUEBLOG_AFTER_URL").unwrap();

    // 服务端口
    let server_port = std::env::var("VUEBLOG_AFTER_PORT")
        .unwrap()
        .parse::<u16>()
        .unwrap();

    (server_address, server_port, workers, db_pool, redis_client)
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    let (server_address, server_port, workers, db_pool, redis_client) = init().await;

    info!("URL: http://{}:{}/", server_address.as_str(), server_port);

    HttpServer::new(move || {
        App::new()
            .wrap(Cors::permissive())
            .app_data(web::Data::new(AppState {
                db_pool: db_pool.clone(),
                redis_pool: redis_client.clone(),
            }))
            .service(blog_list)
            .service(blog_edit)
            .service(blog_detail)
            .service(blog_deletes)
            .service(login)
            .service(sign_token)
            .service(generate_captcha_code)
            .service(all_user)
            .service(user_info)
            .service(user_update)
            .service(user_add)
            .service(user_deletes)
            .service(friend_add)
            .service(friend_all)
            .service(friend_deletes)
            .service(friend_update)
            .service(friend_limit)
            .service(system_info)
            .service(system_update)
            .service(tag_all)
            .service(tag_add)
            .service(tag_delete)
            .service(tag_update)
            .service(tags_blog)
            .service(tag_id_by_name)
            .service(tag_is_exist)
            .service(sort_list)
            .service(sort_remove)
            .service(sort_add)
            .service(sort_update)
            .service(me)
            .service(me_update)
            .default_service(
                web::route()
                    .guard(guard::Not(guard::Get()))
                    .to(not_found_page),
            )
    })
    .workers(workers)
    .bind((server_address, server_port))?
    .run()
    .await
}
