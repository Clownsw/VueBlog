use serde::{Deserialize, Serialize};

/**
 * 分页向量
 */
#[derive(Debug, Deserialize, Serialize)]
pub struct Limit<T> {
    pub datas: Vec<T>,
    pub total: i64,        // 所有个数
    pub page_size: i64,    // 每页个数
    pub currnet_page: i64, // 当前页
    pub pages: i64,        // 总页数
}

impl<T> Limit<T> {
    pub fn from_unknown_datas(
        page_size: i64,
        all_count: i64,
        currnet_page: i64,
        datas: Vec<T>,
    ) -> Limit<T> {
        let total: i64 = all_count;
        let mut pages: i64 = 0;

        if total > 0 {
            if total <= page_size {
                pages = 1;
            } else if total % page_size == 0 {
                pages = total / page_size;
            } else {
                pages = (total / page_size) + 1;
            }
        }

        Self {
            datas,
            total,
            page_size,
            currnet_page,
            pages,
        }
    }
}
