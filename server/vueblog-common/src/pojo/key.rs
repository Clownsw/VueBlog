use serde::{Deserialize, Serialize};

#[derive(Debug, Default, Clone, Serialize, Deserialize)]
pub struct SelectBlogKey {
    pub title: String,
    pub key: String,
}

#[derive(Debug, Default, Clone, Serialize, Deserialize)]
pub struct UpdateBlogKey {
    pub id: i64,
    pub title: String,
    pub key: String,
}
