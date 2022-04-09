use std::sync::{Arc, Mutex};

// 验证码长度
pub const CAPTCHA_CODE_NUM: usize = 5;

lazy_static! {
    pub static ref KEY: Arc<Mutex<String>> = Arc::new(Mutex::new(String::new()));
}
