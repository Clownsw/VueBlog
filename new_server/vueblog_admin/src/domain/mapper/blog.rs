use crate::domain::table::SysBlog;

crud!(SysBlog {});
impl_select_page!(SysBlog { select_all_id_page(table_column: &str) => "" });
