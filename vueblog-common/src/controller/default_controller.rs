use actix_web::{http::StatusCode, HttpResponse, HttpResponseBuilder};

use crate::pojo::msg::ResultMsg;

/**
 * 404错误页面
 */
pub async fn not_found_page() -> HttpResponse<String> {
    HttpResponseBuilder::new(StatusCode::NOT_FOUND)
        .finish()
        .set_body(
            serde_json::to_string(&ResultMsg::<()>::success_message(Some(String::from("404"))))
                .unwrap(),
        )
}
