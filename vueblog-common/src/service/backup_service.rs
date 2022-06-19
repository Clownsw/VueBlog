use sqlx::MySqlPool;

use crate::{
    dao::backup_dao::{select_backup_info, update_backup_info_content},
    pojo::backup::SelectBackUp,
    util::{common_util::to_json_string, sql_util::sql_run_is_success},
};

pub async fn get_backup_info(db_pool: &MySqlPool) -> SelectBackUp {
    let mut backup = SelectBackUp::new();
    match select_backup_info(db_pool).await {
        Ok(v) => {
            if let Ok(v) = serde_json::from_str::<SelectBackUp>(v.as_str()) {
                backup = v;
            }
        }
        _ => {}
    }
    backup
}

pub async fn update_backup_info(db_pool: &MySqlPool, backup: SelectBackUp) -> bool {
    sql_run_is_success(update_backup_info_content(db_pool, to_json_string(&backup).await).await)
        .await
}
