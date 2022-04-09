use actix_cors::Cors;
use actix_web::{guard, web, App, HttpServer};
use log::info;
use sqlx::{MySqlPool, Pool};
use vueblog_common::{
    controller::{
        blog_controller::{blog_detail, blog_list},
        default_controller::not_found_page,
        friend_controller::friend_all,
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
 * 初始化
 */
async fn init() -> (String, u16, usize, MySqlPool) {
    // 加载.env
    dotenv::dotenv().ok();

    // 初始化日志
    pretty_env_logger::init_custom_env("VUEBLOG_BEFORE_LOG_LEVEL");

    // HTTP worker 个数
    let workers = std::env::var("VUEBLOG_BEFORE_WORKERS")
        .unwrap()
        .parse::<usize>()
        .unwrap();

    // 数据库连接池
    let db_pool = make_db_pool().await;

    // 服务地址
    let server_address = std::env::var("VUEBLOG_BEFORE_URL").unwrap();

    // 服务端口
    let server_port = std::env::var("VUEBLOG_BEFORE_PORT")
        .unwrap()
        .parse::<u16>()
        .unwrap();

    (server_address, server_port, workers, db_pool)
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    let (server_address, server_port, workers, db_pool) = init().await;

    info!("URL: http://{}:{}/", server_address.as_str(), server_port);

    HttpServer::new(move || {
        App::new()
            .wrap(Cors::permissive())
            .app_data(web::Data::new(AppState {
                db_pool: db_pool.clone(),
                redis_pool: None,
            }))
            .service(blog_list)
            .service(blog_detail)
            .service(friend_all)
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
