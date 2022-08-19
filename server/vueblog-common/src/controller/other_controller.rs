use actix_web::{get, Responder, web};
use captcha_rust::Captcha;
use crypto::{digest::Digest, sha2::Sha256};

use crate::{
    config::global_config::GLOBAL_CONFIG,
    pojo::{captcha::ResultCaptcha, status::AppState},
    util::{common_util::build_response_ok_data, redis_util},
};

/**
 * 生成验证码
 */
#[get("/api/captcha")]
pub async fn generate_captcha_code(data: web::Data<AppState>) -> impl Responder {
    // 生成随机验证码
    let code = Captcha::new(GLOBAL_CONFIG.get().unwrap().captcha_code_num, 100, 50);

    // 对验证码生成唯一值
    let mut hasher = Sha256::new();
    hasher.input_str(format!("{}{}", code.text, code.base_img).as_str());
    let result = hasher.result_str();

    // 将验证码保存在redis, TTL为60秒
    let mut async_conn = data.redis_pool.get().await.unwrap();
    redis_util::set_and_ttl(
        &mut async_conn,
        result.clone(),
        code.text.clone(),
        60 * 1000,
    )
        .await;

    // 将验证码返回
    build_response_ok_data(ResultCaptcha {
        id: result,
        base64_url: code.base_img,
    })
        .await
}
