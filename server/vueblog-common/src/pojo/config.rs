#[repr(C)]
pub struct GlobalConfig {
    pub jwt_key: String,
    // 验证码长度
    pub captcha_code_num: usize,
    // 后台管理博文列表每页个数
    pub blog_limit_num: i64,
    // 后台管理友联列表每页个数
    pub friend_limit_num: i64,
    // 前台展示标签博文列表每页展示
    pub blog_tag_limit_num: i64,
    // 前台展示分类博文每页展示
    pub blog_sort_limit_num: i64,
    // 搜索引擎地址
    pub search_server: String,
    // 搜索引擎秘钥
    pub search_key: String,
    // 搜索结果最大长度
    pub search_limit: usize,
}

impl GlobalConfig {
    pub const fn new() -> Self {
        Self {
            jwt_key: String::new(),
            captcha_code_num: 5,
            blog_limit_num: 10,
            friend_limit_num: 5,
            blog_tag_limit_num: 5,
            blog_sort_limit_num: 5,
            search_server: String::new(),
            search_key: String::new(),
            search_limit: 50,
        }
    }
}
