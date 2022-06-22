use crate::{
    config::global_config::GLOBAL_CONFIG,
    pojo::{claims::Claims, user::TokenUser},
};
use chrono::{Duration, Utc};
use jsonwebtoken::{
    decode, encode, Algorithm, DecodingKey, EncodingKey, Header, TokenData, Validation,
};
use serde::{de::DeserializeOwned, Serialize};

/**
 * 以默认算法生成token
 */
pub async fn get_token_default<T: Serialize>(
    claims: T,
) -> Result<String, jsonwebtoken::errors::Error> {
    let token = encode(
        &Header::default(),
        &claims,
        &EncodingKey::from_secret(unsafe { GLOBAL_CONFIG.jwt_key.as_bytes() }),
    );

    token
}

/**
 * 以默认算法解密token
 */
pub async fn sign_token_default<T>(token: &str) -> Result<TokenData<T>, jsonwebtoken::errors::Error>
where
    T: DeserializeOwned,
{
    let token_message = decode(
        token,
        &DecodingKey::from_secret(unsafe { GLOBAL_CONFIG.jwt_key.as_bytes() }),
        &Validation::new(Algorithm::HS256),
    );

    token_message
}

/**
 * 判断token是否过期
 */
pub async fn is_token_expried<T: std::cmp::PartialEq + DeserializeOwned>(
    claims: &Claims<T>,
) -> bool {
    if claims.exp > Utc::now().timestamp_millis() as usize {
        return true;
    }
    false
}

/**
 * 以默认算法生成token, Data=TokenUser
 */
pub async fn get_token_default_token_user(token_user: Option<TokenUser>) -> String {
    get_token_default::<Claims<TokenUser>>(Claims {
        exp: Utc::now()
            .checked_add_signed(Duration::days(1))
            .unwrap()
            .timestamp_millis() as usize,
        data: token_user,
    })
    .await
    .unwrap()
}
