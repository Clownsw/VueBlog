use serde::{Deserialize, Serialize};

#[derive(Debug, Deserialize, Serialize)]
pub struct SelectTag {
    pub id: i64,
    pub name: String,
}

#[derive(Deserialize, Serialize)]
pub struct Tag {
    pub name: Option<String>,
}

#[derive(Deserialize, Serialize)]
pub struct InsertTag {
    pub name: String,
}

#[derive(Deserialize, Serialize)]
pub struct UpdateTag {
    pub id: i64,
    pub name: String,
}