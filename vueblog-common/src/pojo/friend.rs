use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize)]
pub struct SelectFriend {
    pub id: i64,
    pub name: String,
    pub description: Option<String>,
    pub href: String,
    pub avatar: String,
}

#[derive(Serialize, Deserialize)]
pub struct InsertFriend {
    pub name: String,
    pub description: Option<String>,
    pub href: String,
    pub avatar: String,
}

#[derive(Serialize, Deserialize)]
pub struct UpdateFriend {
    pub id: i64,
    pub name: String,
    pub description: String,
    pub href: String,
    pub avatar: String,
}