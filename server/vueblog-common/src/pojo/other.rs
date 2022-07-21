use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize)]
pub struct Void {}

impl Void {
    pub fn new() -> Self {
        Void {}
    }
}

#[derive(Serialize, Deserialize, Clone)]
pub struct SelectPageFooter {
    pub content: String,
}

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct UpdatePageFooter {
    pub content: String,
}

#[derive(Debug, Default, Clone, sqlx::FromRow)]
pub struct SelectCount {
    pub count: i64,
}

#[derive(Debug, Default, Clone, Serialize, Deserialize, sqlx::FromRow)]
pub struct SelectId {
    pub id: i64,
}
