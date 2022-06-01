use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize, Clone)]
pub struct SelectSort {
    pub id: i32,
    pub order: i32,
    pub name: String,
}

#[derive(Serialize, Deserialize, Clone)]
pub struct SelectSortWithBlog {
    pub id: Option<i32>,
    pub order: Option<i32>,
    pub name: Option<String>,
}

#[derive(Serialize, Deserialize, Clone)]
pub struct InsertSort {
    pub name: String,
}

#[derive(Serialize, Deserialize, Clone)]
pub struct UpdateSort {
    pub id: i32,
    pub order: i32,
    pub name: String,
}
