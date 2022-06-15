use crate::{
    config::global_config::GLOBAL_CONFIG,
    pojo::{captcha::ResultCaptcha, status::AppState},
    util::{
        common_util::{build_response_ok_data, build_response_ok_message},
        error_util, redis_util,
    },
};
use actix_web::{get, web, Responder};
use captcha_rust::Captcha;
use crypto::{digest::Digest, sha2::Sha256};
use sqlx::MySqlPool;

/**
 * 生成验证码
 */
#[get("/api/captcha")]
pub async fn generate_captcha_code(data: web::Data<AppState>) -> impl Responder {
    // 生成随机验证码
    let code = unsafe { Captcha::new(GLOBAL_CONFIG.captcha_code_num, 100, 50) };

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

#[get("/tran_test")]
pub async fn transactional_test(data: web::Data<AppState>) -> impl Responder {
    transaction_manager(&data.db_pool).await;

    build_response_ok_message(String::from(error_util::SUCCESS)).await
}

pub async fn transaction_manager(db: &MySqlPool) {
    let s = |db: &MySqlPool| {
        let db_clone = db.clone();

        Box::pin(async move {
            let mut tran = db_clone.begin().await.unwrap();

            sqlx::query!("INSERT INTO m_other(`order`, title, content) VALUES(0, 'test', 'test')")
                .execute(&mut tran)
                .await
                .unwrap();

            tran.commit().await.unwrap();
        })
    };

    (s(db)).await;
}
