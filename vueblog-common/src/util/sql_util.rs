use sqlx::mysql::MySqlQueryResult;

/**
 * 判断增、删、改, 执行是否成功
 */
pub async fn sql_run_is_success(result: Result<MySqlQueryResult, sqlx::Error>) -> bool {
    match result {
        Ok(v) => {
            if v.rows_affected() > 0 {
                return true;
            }
        }
        Err(_) => (),
    };

    false
}

/**
 * 生成问号
 */
pub async fn build_what_sql_by_num(n: usize) -> String {
    (0..n).map(|_| "?").collect::<Vec<&str>>().join(",")
}
