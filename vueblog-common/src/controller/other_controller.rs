use crate::{
    config::global_config,
    pojo::{captcha::ResultCaptcha, msg::ResultMsg, status::AppState},
    util::error_util,
};
use actix_web::{get, web, Responder};
use captcha_rust::Captcha;
use crypto::{digest::Digest, sha2::Sha256};
use redis::AsyncCommands;

/**
 * 生成验证码
 */
#[get("/api/captcha")]
pub async fn generate_captcha_code(data: web::Data<AppState>) -> impl Responder {
    // 生成随机验证码
    let code = Captcha::new(global_config::CAPTCHA_CODE_NUM, 100, 50);

    // 对验证码生成唯一值
    let mut hasher = Sha256::new();
    hasher.input_str(format!("{}{}", code.text, code.base_img).as_str());
    let result = hasher.result_str();

    // 将验证码保存在redis, TTL为60秒
    let mut async_conn = data.redis_pool.as_ref().unwrap().get().await.unwrap();
    async_conn
        .pset_ex::<_, _, ()>(result.clone(), code.text.clone().to_lowercase(), 60 * 1000)
        .await
        .unwrap();

    // 将验证码返回
    serde_json::to_string(&ResultMsg::<ResultCaptcha>::success_all(
        200,
        Some(String::from(error_util::SUCCESS)),
        Some(ResultCaptcha {
            id: result,
            base64_url: code.base_img,
        }),
    ))
    .unwrap()
}
