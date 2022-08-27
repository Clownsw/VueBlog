use rbatis::sql::PageRequest;
use serde::{Deserialize, Serialize};

/// 博文分页DTO
#[derive(Serialize, Deserialize, Clone, Debug)]
pub struct BlogPageDTO {
    pub page_no: u64,
    pub page_size: u64,
}

impl Into<PageRequest> for BlogPageDTO {
    fn into(self) -> PageRequest {
        PageRequest::new(self.page_no, self.page_size)
    }
}
