use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize)]
pub struct Void {}

#[derive(Serialize, Deserialize, Clone)]
pub struct SelectPageFooter {
    pub content: String,
}

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct UpdatePageFooter {
    pub content: String,
}
