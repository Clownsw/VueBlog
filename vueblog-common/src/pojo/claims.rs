use serde::{Deserialize, Serialize};

#[derive(Debug, PartialEq, Serialize, Deserialize)]
pub struct Claims<T: PartialEq> {
    pub exp: usize,
    pub data: Option<T>,
}
