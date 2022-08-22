use crate::domain::vo::user::SysUserVO;
use crate::domain::vo::SysRoleVO;
use serde::{Deserialize, Serialize};

///登录数据
#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct SignInVO {
    pub user: Option<SysUserVO>,
    pub permissions: Vec<String>,
    pub access_token: String,
    pub role: Option<SysRoleVO>,
}

impl ToString for SignInVO {
    fn to_string(&self) -> String {
        serde_json::json!(self).to_string()
    }
}
