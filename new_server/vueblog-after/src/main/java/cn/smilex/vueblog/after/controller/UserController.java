package cn.smilex.vueblog.after.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.service.UserService;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.server.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author smilex
 * @date 2022/12/4/15:49
 * @since 1.0
 */
@SuppressWarnings("unused")
@Slf4j
@PathPrefix("/user")
@RequestConverter(JacksonRequestConverterFunction.class)
@ProducesJson
@CrossOrigin
@Component
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取当前token中的用户信息
     *
     * @param request 请求对象
     * @return 用户信息
     */
    @Options("/info")
    @Post("/info")
    public Result<?> info(HttpRequest request) {
        return CommonUtil.tryRun(
                () -> userService.getUserInfoByToken(request),
                Optional.of(
                        e -> Optional.of(e.getMessage())
                )
        );
    }
}
