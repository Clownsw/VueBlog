use serde::{Deserialize, Serialize};

use crate::pojo::{other::UpdatePageFooter, system::UpdateSystem};

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct SystemFormVo {
    pub system: UpdateSystem,
    pub footer: UpdatePageFooter,
}
