use std::env;

use once_cell::sync::OnceCell;

use crate::pojo::config::GlobalConfig;

// 全局配置
pub static GLOBAL_CONFIG: OnceCell<GlobalConfig> = OnceCell::new();

/**
 * 初始化全局配置
 */
pub async fn init_global_config() -> Result<(), anyhow::Error> {
    let mut global_config = GlobalConfig::new();
    global_config.jwt_key = env::var("JWT_KEY")?;
    global_config.captcha_code_num = env::var("CAPTCHA_CODE_NUM")?.parse::<usize>()?;
    global_config.blog_limit_num = env::var("BLOG_LIMIT_NUM")?.parse::<i64>()?;
    global_config.friend_limit_num = env::var("FRIEND_LIMIT_NUM")?.parse::<i64>()?;
    global_config.blog_tag_limit_num = env::var("BLOG_TAG_LIMIT_NUM")?.parse::<i64>()?;
    global_config.blog_sort_limit_num = env::var("BLOG_SORT_LIMIT_NUM")?.parse::<i64>()?;
    global_config.search_server = env::var("SEARCH_SERVER")?;
    global_config.search_key = env::var("SEARCH_KEY")?;
    global_config.search_limit = env::var("SEARCH_LIMIT")?.parse::<usize>()?;
    GLOBAL_CONFIG.set(global_config);
    Ok(())
}
