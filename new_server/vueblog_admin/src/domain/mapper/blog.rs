use crate::domain::table::SysBlog;

crud!(SysBlog {});
impl_select_page!(SysBlog { select_page() => ""});
