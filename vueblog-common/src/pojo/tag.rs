use serde::{Deserialize, Serialize};

#[derive(Deserialize, Serialize)]
pub struct Tag {
    pub id: Option<i64>,
    pub name: Option<String>,
}
