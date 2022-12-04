package cn.smilex.vueblog.common.service;

import cn.smilex.vueblog.common.entity.user.LoginUser;
import com.linecorp.armeria.common.HttpRequest;

/**
 * @author smilex
 * @date 2022/12/3/23:18
 */
public interface LoginService {

    /**
     * 登录用户
     *
     * @param loginUser 用户
     * @param request   请求对象
     * @return 结果
     */
    String login(LoginUser loginUser, HttpRequest request);
}