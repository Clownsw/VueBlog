pub mod controller;

use vueblog_common::pojo::status::AppState;

use actix_cors::Cors;
use actix_web::{web, App, HttpServer};
use sqlx::{MySqlPool, Pool};

use controller::blog_controller::{blog_detail, blog_list};

/**
 * 初始化数据库连接池
 */
async fn make_db_pool() -> MySqlPool {
    let sql_url = std::env::var("DATABASE_URL").unwrap();
    Pool::connect(&sql_url).await.unwrap()
}

/**
 * 初始化
 */
async fn init() -> (String, u16, MySqlPool) {
    // 加载.env
    dotenv::dotenv().ok();

    // 初始化日志
    pretty_env_logger::init_custom_env("VUEBLOG_BEFORE_LOG_LEVEL");

    // 数据库连接池
    let db_pool = make_db_pool().await;

    // 服务地址
    let server_address = std::env::var("VUEBLOG_BEFORE_URL").unwrap();

    // 服务端口
    let server_port = std::env::var("VUEBLOG_BEFORE_PORT")
        .unwrap()
        .parse::<u16>()
        .unwrap();

    (server_address, server_port, db_pool)
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    let (server_address, server_port, db_pool) = init().await;

    HttpServer::new(move || {
        App::new()
            .wrap(Cors::permissive())
            .app_data(web::Data::new(AppState {
                db_pool: db_pool.clone(),
                redis_pool: None,
            }))
            .service(blog_list)
            .service(blog_detail)
    })
    .bind((server_address, server_port))?
    .run()
    .await
}
