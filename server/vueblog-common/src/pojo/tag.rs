use serde::{Deserialize, Serialize};

#[derive(Debug, Deserialize, Serialize, Clone)]
pub struct SelectTag {
    pub id: i64,
    pub name: String,
}

#[derive(Debug, Deserialize, Serialize, Clone)]
pub struct SelectBlogOther {
    pub id: Option<i64>,
    pub name: Option<String>,
}

#[derive(Debug, Deserialize, Serialize, Clone)]
pub struct Tag {
    pub name: Option<String>,
}

impl Into<String> for Tag {
    fn into(self) -> String {
        match self.name {
            Some(v) => v,
            None => String::new(),
        }
    }
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
