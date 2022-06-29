use serde::{Deserialize, Serialize};

#[derive(Debug, Default, Clone, Serialize, Deserialize)]
pub struct SelectBlogKey {
    pub id: i64,
    pub key: String,
}

#[derive(Debug, Default, Clone, Serialize, Deserialize)]
pub struct UpdateBlogKey {
    pub id: i64,
    pub key: String,
}
