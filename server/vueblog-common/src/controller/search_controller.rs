use actix_web::{get, HttpRequest, Responder, web};
use meilisearch_sdk::search::Selectors;
use qstring::QString;

use crate::config::global_config::GLOBAL_CONFIG;
use crate::pojo::blog::SearchBlogResult;
use crate::pojo::status::AppState;
use crate::util::common_util::{build_response_ok_data, build_response_ok_message, search_result_vec_to_vec};
use crate::util::error_util;

///
/// 搜索博文
///
#[get("/search")]
pub async fn search_blog(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    let qs = QString::from(req.query_string());

    if let Some(v) = qs.get("q") {
        let blog_index = data.search_client.index("blog");
        if let Ok(result) = blog_index.search()
            .with_query(v)
            .with_attributes_to_highlight(Selectors::Some(&["id", "title", "description"]))
            .with_limit(GLOBAL_CONFIG.get().unwrap().search_limit)
            .execute::<SearchBlogResult>()
            .await {
            return build_response_ok_data(search_result_vec_to_vec(result.hits).await).await;
        }
    }

    build_response_ok_message(String::from(error_util::ERROR_REQUEST_PARAM)).await
}