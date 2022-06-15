use std::{
    env,
    sync::{Arc, Mutex},
};

use crate::pojo::config::GlobalConfig;

// 全局配置
pub static mut GLOBAL_CONFIG: GlobalConfig = GlobalConfig::new();

/**
 * 初始化全局配置
 */
pub async fn init_global_config() -> Result<(), anyhow::Error> {
    unsafe {
        GLOBAL_CONFIG.captcha_code_num = env::var("CAPTCHA_CODE_NUM")?.parse::<usize>()?;
        GLOBAL_CONFIG.blog_limit_num = env::var("BLOG_LIMIT_NUM")?.parse::<i64>()?;
        GLOBAL_CONFIG.friend_limit_num = env::var("FRIEND_LIMIT_NUM")?.parse::<i64>()?;
        GLOBAL_CONFIG.blog_tag_limit_num = env::var("BLOG_TAG_LIMIT_NUM")?.parse::<i64>()?;
        GLOBAL_CONFIG.blog_sort_limit_num = env::var("BLOG_SORT_LIMIT_NUM")?.parse::<i64>()?;
    }

    Ok(())
}

lazy_static! {
    pub static ref KEY: Arc<Mutex<String>> = Arc::new(Mutex::new(String::new()));
}
