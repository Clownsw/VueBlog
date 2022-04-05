use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize, Clone)]
pub struct SelectFriend {
    pub id: i64,
    pub name: String,
    pub href: String,
}

#[derive(Serialize, Deserialize, Clone)]
pub struct InsertFriend {
    pub name: String,
    pub href: String,
}
