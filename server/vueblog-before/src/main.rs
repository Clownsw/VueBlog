use actix_cors::Cors;
use actix_web::{App, HttpServer, web};
use actix_web::guard;
use log::info;
use meilisearch_sdk::client::Client;
use redis_async_pool::{RedisConnectionManager, RedisPool};
use sqlx::{MySqlPool, Pool};

use vueblog_common::{
    config::global_config::init_global_config,
    controller::{
        blog_controller::{blog_detail, blog_detail_id_key, blog_list, blog_sort_list, blog_tag_list},
        default_controller::not_found_page,
        friend_controller::friend_all,
        me_controller::me,
        search_controller::search_blog,
        sort_controlller::sort_list,
        system_controller::{page_footer, system_info},
        tag_controller::tags_blog,
    },
    pojo::status::AppState,
};
use vueblog_common::config::global_config::GLOBAL_CONFIG;

///
/// 初始化数据库连接池
///
async fn make_db_pool() -> MySqlPool {
    let sql_url = std::env::var("DATABASE_URL").unwrap();
    Pool::connect(&sql_url).await.unwrap()
}

///
/// 初始化redis客户端
///
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

///
/// 初始化搜索引擎客户端
///
async fn make_search_client() -> Client {
    let global_config = GLOBAL_CONFIG.get().unwrap();
    Client::new(&global_config.search_server, &global_config.search_key)
}

///
/// 初始化
///
async fn init() -> (String, u16, usize, MySqlPool, RedisPool, Client) {
    // 加载.env
    dotenv::dotenv().ok();

    // 初始化日志
    pretty_env_logger::init_custom_env("VUEBLOG_BEFORE_LOG_LEVEL");

    // HTTP worker 个数
    let workers = std::env::var("VUEBLOG_BEFORE_WORKERS")
        .unwrap()
        .parse::<usize>()
        .unwrap();

    if let Err(_) = init_global_config().await {
        panic!("init global config error!")
    }

    // 数据库连接池
    let db_pool = make_db_pool().await;

    // redis客户端
    let redis_client = make_redis_client().await;

    // 搜索引擎客户端
    let search_client = make_search_client().await;

    // 服务地址
    let server_address = std::env::var("VUEBLOG_BEFORE_URL").unwrap();

    // 服务端口
    let server_port = std::env::var("VUEBLOG_BEFORE_PORT")
        .unwrap()
        .parse::<u16>()
        .unwrap();

    (server_address, server_port, workers, db_pool, redis_client, search_client)
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    let (server_address, server_port, workers, db_pool, redis_client, search_client) = init().await;

    info!("URL: http://{}:{}/", server_address.as_str(), server_port);

    HttpServer::new(move || {
        App::new()
            .wrap(Cors::permissive())
            .app_data(web::Data::new(AppState {
                db_pool: db_pool.clone(),
                redis_pool: redis_client.clone(),
                search_client: search_client.clone(),
            }))
            .service(blog_list)
            .service(blog_detail)
            .service(blog_detail_id_key)
            .service(blog_sort_list)
            .service(blog_tag_list)
            .service(me)
            .service(friend_all)
            .service(system_info)
            .service(page_footer)
            .service(tags_blog)
            .service(sort_list)
            .service(search_blog)
            .default_service(
                web::route()
                    .guard(guard::Any(guard::Get()).or(guard::Post()))
                    .to(not_found_page),
            )
    })
        .workers(workers)
        .bind((server_address, server_port))?
        .run()
        .await
}
