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
