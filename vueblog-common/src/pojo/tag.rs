use serde::{Deserialize, Serialize};

#[derive(Deserialize, Serialize)]
pub struct Tag {
    pub name: Option<String>,
}
