use actix_web::{get, web, HttpRequest, Responder};

use crate::{
    dao::staticstics_dao::{select_blog_statistics, select_statistics},
    pojo::{other::Void, status::AppState, tuple::Tuple},
    util::{
        common_util::{
            build_response_baq_request_message, build_response_ok_data, security_interceptor_aop,
        },
        error_util,
    },
};

#[get("/statistics")]
pub async fn statistics(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, _, _, _| {
            let db_pool_clone = app_state.db_pool.clone();

            Box::pin(async move {
                let mut resp =
                    build_response_baq_request_message(String::from(error_util::ERROR)).await;

                if let Ok(v) = select_statistics(&db_pool_clone).await {
                    resp = build_response_ok_data(v).await;
                }

                resp
            })
        },
        &req,
        &data,
        None,
        None,
    )
    .await
}

#[get("/statistics/blog")]
pub async fn statistics_blog(req: HttpRequest, data: web::Data<AppState>) -> impl Responder {
    security_interceptor_aop::<_, Void>(
        move |app_state, _, _, _| {
            let db_pool_clone = app_state.db_pool.clone();

            Box::pin(async move {
                let mut resp =
                    build_response_baq_request_message(String::from(error_util::ERROR)).await;

                if let Ok(mut v) = select_blog_statistics(&db_pool_clone).await {
                    let mut data: Tuple<Vec<String>, Vec<i64>> = Tuple::default();

                    v.reverse();

                    for item in v.iter() {
                        data.left.push(item.day.clone());
                        data.right.push(item.view_count);
                    }

                    resp = build_response_ok_data(data).await;
                }

                resp
            })
        },
        &req,
        &data,
        None,
        None,
    )
    .await
}
