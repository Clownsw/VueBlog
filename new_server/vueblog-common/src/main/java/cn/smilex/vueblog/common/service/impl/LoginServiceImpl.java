package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.common.HashMapBuilder;
import cn.smilex.vueblog.common.entity.user.LoginUser;
import cn.smilex.vueblog.common.entity.user.User;
import cn.smilex.vueblog.common.exception.VueBlogException;
import cn.smilex.vueblog.common.service.LoginService;
import cn.smilex.vueblog.common.service.UserService;
import cn.smilex.vueblog.common.util.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linecorp.armeria.common.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author smilex
 * @date 2022/12/3/23:18
 */
@SuppressWarnings("unused")
@Service
public class LoginServiceImpl implements LoginService {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 登录用户
     *
     * @param loginUser 用户
     * @param request   请求对象
     * @return 结果
     */
    @Override
    public String login(LoginUser loginUser, HttpRequest request) {
        User user = userService.getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUserName, loginUser.getUserName())
        );

        if (user == null) {
            throw new VueBlogException(ResultCode.ERROR_LOGIN_NOT_FOUND_USER);
        }

        if (!user.getPassword().equals(loginUser.getPassword())) {
            throw new VueBlogException(ResultCode.ERROR_LOGIN_PASSWORD_ERROR);
        }

        return JwtUtils.createJWTToken(
                new HashMapBuilder<String, Object>(3)
                        .put("id", user.getId())
                        .put("username", user.getUserName())
                        .put("status", user.getStatus())
                        .getMap()
        );
    }
}
