use actix_cors::Cors;
use actix_web::{guard, web, App, HttpServer};
use log::info;
use redis_async_pool::{RedisConnectionManager, RedisPool};
use sqlx::{MySqlPool, Pool};
use vueblog_common::{
    controller::{
        blog_controller::{blog_detail, blog_list, blog_sort_list},
        default_controller::not_found_page,
        friend_controller::friend_all,
        me_controller::me,
        sort_controlller::sort_list,
        system_controller::system_info,
        tag_controller::tags_blog,
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
 * 初始化
 */
async fn init() -> (String, u16, usize, MySqlPool, RedisPool) {
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

    // redis客户端
    let redis_client = make_redis_client().await;

    // 服务地址
    let server_address = std::env::var("VUEBLOG_BEFORE_URL").unwrap();

    // 服务端口
    let server_port = std::env::var("VUEBLOG_BEFORE_PORT")
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
            .service(me)
            .service(blog_detail)
            .service(blog_sort_list)
            .service(friend_all)
            .service(system_info)
            .service(tags_blog)
            .service(sort_list)
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
