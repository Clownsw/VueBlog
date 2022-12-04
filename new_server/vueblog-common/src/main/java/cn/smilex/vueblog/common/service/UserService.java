package cn.smilex.vueblog.common.service;

import cn.smilex.vueblog.common.entity.user.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linecorp.armeria.common.HttpRequest;

/**
 * @author smilex
 * @date 2022/11/12/11:45
 * @since 1.0
 */
@SuppressWarnings("unused")
public interface UserService extends IService<User> {

    /**
     * 判断当前请求是否包含token以及token有效性来判定是否登录
     *
     * @param request 请求对象
     * @return 是否登录
     */
    boolean isLogin(HttpRequest request);

    /**
     * 获取当前token中的用户信息
     *
     * @param request 请求对象
     * @return 用户信息
     */
    User getUserInfoByToken(HttpRequest request);
}
