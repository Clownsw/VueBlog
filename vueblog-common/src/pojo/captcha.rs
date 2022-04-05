use serde::{Deserialize, Serialize};

/**
 * 验证码响应结构
 */
#[derive(Debug, Serialize, Deserialize)]
pub struct ResultCaptcha {
    pub id: String,
    pub base64_url: String,
}
