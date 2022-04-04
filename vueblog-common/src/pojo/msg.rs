use serde::{Deserialize, Serialize};

/**
 * 向前端响应的实体
 */
#[derive(Debug, Deserialize, Serialize)]
pub struct ResultMsg<T> {
    pub code: i32,
    pub message: Option<String>,
    pub data: Option<T>,
}

impl<T> ResultMsg<T> {
    /**
     * 操作成功
     */
    pub fn success(data: Option<T>) -> ResultMsg<T> {
        Self::success_all(200, None, data)
    }

    /**
     * 操作成功, 只返回提示信息
     */
    pub fn success_message(message: Option<String>) -> ResultMsg<T> {
        Self::success_all(200, message, None)
    }

    /**
     * 操作成功
     */
    pub fn success_all(code: i32, message: Option<String>, data: Option<T>) -> ResultMsg<T> {
        ResultMsg::<T> {
            code,
            message,
            data,
        }
    }

    /**
     * 操作非常规错误, 只返回提示信息
     */
    pub fn fail_msg(message: Option<String>) -> ResultMsg<T> {
        Self::fail_all(400, message, None)
    }

    /**
     * 操作非常规错误
     */
    pub fn fail_message_data(message: Option<String>, data: Option<T>) -> ResultMsg<T> {
        Self::fail_all(400, message, data)
    }

    /**
     * 操作非常规错误
     */
    pub fn fail_all(code: i32, message: Option<String>, data: Option<T>) -> ResultMsg<T> {
        ResultMsg::<T> {
            code,
            message,
            data,
        }
    }
}
