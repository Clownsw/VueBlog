use rbatis::sql::{Page, PageRequest};

use crate::domain::table::SysBlog;
use crate::error::Result;
use crate::pool;

/// 系统文章服务
pub struct SysBlogService {}

impl SysBlogService {
    pub async fn select_id_page(&self, page: PageRequest) -> Result<Page<SysBlog>> {
        Ok(SysBlog::select_all_id_page(pool!(), &page, "id").await?)
    }
}
