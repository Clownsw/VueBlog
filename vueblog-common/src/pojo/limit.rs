use serde::{Deserialize, Serialize};

/**
 * 分页向量
 */
#[derive(Debug, Deserialize, Serialize)]
pub struct Limit<T> {
    pub datas: Vec<T>,
    pub total: i64,   // 所有个数
    pub size: i64,    // 每页个数
    pub current: i64, // 当前页
    pub pages: i64,   // 总页数
}

impl<T> Limit<T> {
    pub fn from_unknown_datas(size: i64, all_count: i64, current: i64, datas: Vec<T>) -> Limit<T> {
        let total: i64 = all_count;
        let mut pages: i64 = 0;

        {
            if total > 0 {
                if total % size == 0 {
                    pages = total / size;
                } else {
                    pages = (total / size) + 1;
                }
            }
        }

        Self {
            datas,
            total,
            size,
            current,
            pages,
        }
    }
}
