package cn.smilex.vueblog.common.service;

import cn.smilex.vueblog.common.annotation.cache.Cache;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.entity.user.LoginUser;
import com.linecorp.armeria.common.HttpRequest;

import java.util.Optional;

/**
 * @author smilex
 * @date 2022/12/3/23:18
 */
@SuppressWarnings("all")
public interface LoginService {

    /**
     * 登录用户
     *
     * @param loginUser 用户
     * @param request   请求对象
     * @return 结果
     */
    @Cache(name = "testCache")
    String login(LoginUser loginUser, HttpRequest request);
}
