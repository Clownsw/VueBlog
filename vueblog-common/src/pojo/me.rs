use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize, Clone)]
pub struct SelectMe {
    pub id: i64,
    pub title: String,
    pub content: String,
}

#[derive(Serialize, Deserialize, Clone)]
pub struct UpdateMe {
    pub id: i64,
    pub title: String,
    pub content: String,
}
