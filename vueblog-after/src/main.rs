pub mod controller;

use actix_cors::Cors;
use actix_web::{web, App, HttpServer};
use log::info;
use redis_async_pool::{RedisConnectionManager, RedisPool};
use sqlx::{MySqlPool, Pool};

use controller::blog_controller::{blog_delete, blog_edit};
use controller::login_controller::{login, sign_token};
use vueblog_common::controller::{
    blog_controller::blog_list,
    other_controller::generate_captcha_code,
    user_controller::{all_user, user_info},
};
use vueblog_common::pojo::status::AppState;

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
 * 初始化
 */
async fn init() -> (String, u16, MySqlPool, RedisPool) {
    // 加载.env
    dotenv::dotenv().ok();

    // 初始化日志
    pretty_env_logger::init_custom_env("VUEBLOG_AFTER_LOG_LEVEL");

    // 数据库连接池
    let db_pool = make_db_pool().await;

    // redis客户端
    let redis_client = make_redis_client().await;

    // 服务地址
    let server_address = std::env::var("VUEBLOG_AFTER_URL").unwrap();

    // 服务端口
    let server_port = std::env::var("VUEBLOG_AFTER_PORT")
        .unwrap()
        .parse::<u16>()
        .unwrap();

    (server_address, server_port, db_pool, redis_client)
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    let (server_address, server_port, db_pool, redis_client) = init().await;

    info!("URL: http://{}:{}/", server_address.as_str(), server_port);

    HttpServer::new(move || {
        App::new()
            .wrap(Cors::permissive())
            .app_data(web::Data::new(AppState {
                db_pool: db_pool.clone(),
                redis_pool: Some(redis_client.clone()),
            }))
            .service(blog_list)
            .service(blog_delete)
            .service(blog_edit)
            .service(login)
            .service(sign_token)
            .service(generate_captcha_code)
            .service(all_user)
            .service(user_info)
    })
    .bind((server_address, server_port))?
    .run()
    .await
}
