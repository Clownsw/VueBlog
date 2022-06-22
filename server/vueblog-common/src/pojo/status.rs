use redis_async_pool::RedisPool;
use sqlx::mysql::MySqlPool;

/**
 * 共享状态实体
 */
pub struct AppState {
    pub db_pool: MySqlPool,
    pub redis_pool: RedisPool,
}
