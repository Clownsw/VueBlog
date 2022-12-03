package cn.smilex.vueblog.after.controller;

import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.entity.user.LoginUser;
import cn.smilex.vueblog.common.service.LoginService;
import cn.smilex.vueblog.common.util.ClassUtil;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.server.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author smilex
 * @date 2022/12/3/23:17
 */
@SuppressWarnings("unused")
@Slf4j
@PathPrefix("/admin")
@RequestConverter(JacksonRequestConverterFunction.class)
@Component
public class LoginController {
    private LoginService loginService;

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 登录用户
     *
     * @param loginUser 用户
     * @return token
     */
    @Post("/login")
    @ProducesJson
    public Result<?> login(Optional<LoginUser> loginUser, HttpRequest request) throws IllegalAccessException {
        if (!loginUser.isPresent() || ClassUtil.objIsNull(LoginUser.class, loginUser.get())) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR);
        }

        return CommonUtil.tryRun(
                () -> loginService.login(loginUser.get(), request),
                Optional.of(
                        e -> Optional.of(e.getMessage())
                )
        );
    }
}
