package cn.smilex.vueblog.common.service;

import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.entity.user.LoginUser;
import com.linecorp.armeria.common.HttpRequest;

import java.util.Optional;

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

    /**
     * 验证token有效性
     *
     * @param token token
     * @return 结果
     */
    Result<?> signToken(Optional<String> token);
}
