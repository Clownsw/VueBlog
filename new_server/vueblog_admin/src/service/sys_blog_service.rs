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
}