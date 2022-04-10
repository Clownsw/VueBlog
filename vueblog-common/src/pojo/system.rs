use serde::{Deserialize, Serialize};

#[derive(Deserialize, Serialize)]
pub struct SelectSystem {
    pub id: i32,
    pub title: String,
    pub description: String,
}

#[derive(Deserialize, Serialize)]
pub struct UpdateSystem {
    pub id: i32,
    pub title: String,
    pub description: String,
}
