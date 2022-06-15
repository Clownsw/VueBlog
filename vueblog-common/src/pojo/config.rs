#[repr(C)]
pub struct GlobalConfig {
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
}

impl GlobalConfig {
    pub const fn new() -> Self {
        Self {
            captcha_code_num: 5,
            blog_limit_num: 10,
            friend_limit_num: 5,
            blog_tag_limit_num: 5,
            blog_sort_limit_num: 5,
        }
    }
}
