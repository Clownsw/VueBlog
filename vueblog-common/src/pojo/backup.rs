use serde::{Deserialize, Serialize};

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct SelectBackUp {
    pub username: String,
    pub password: String,
    pub operator: String,
    pub operator_password: String,
    pub bucket_name: String,
}

impl SelectBackUp {
    pub fn new() -> Self {
        Self {
            username: String::new(),
            password: String::new(),
            operator: String::new(),
            operator_password: String::new(),
            bucket_name: String::new(),
        }
    }
}
