use rbatis::sql::{Page, PageRequest};

use crate::domain::table::SysBlog;
use crate::error::Result;
use crate::pool;

/// 系统文章服务
pub struct SysBlogService {}

impl SysBlogService {
    pub async fn select_all(&self) -> Result<Vec<SysBlog>> {
        let result = SysBlog::select_all(pool!()).await?;

        Ok(result)
    }

    pub async fn select_page(&self) -> Result<Page<SysBlog>> {
        let page_request = PageRequest::new(1, 5);
        Ok(SysBlog::select_page(pool!(), &page_request).await?)
    }
}
