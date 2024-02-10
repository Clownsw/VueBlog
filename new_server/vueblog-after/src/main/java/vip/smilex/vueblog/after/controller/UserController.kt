package vip.smilex.vueblog.after.controller

import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.server.annotation.*
import org.springframework.stereotype.Component
import vip.smilex.vueblog.common.annotation.CrossOrigin
import vip.smilex.vueblog.common.entity.common.Result
import vip.smilex.vueblog.common.service.UserService

/**
 * @author smilex
 * @date 2023/12/3 17:35:46
 */
@Suppress("unused")
@PathPrefix("/user")
@RequestConverter(JacksonRequestConverterFunction::class)
@Decorator(vip.smilex.vueblog.common.handler.AuthService::class)
@ProducesJson
@CrossOrigin
@Component
class UserController(private val userService: UserService) {

    /**
     * 获取当前token中的用户信息
     *
     * @param request 请求对象
     * @return 用户信息
     */
    @Post("/info")
    @Options("/info")
    fun info(request: HttpRequest): Result<*> {
        return Result.success(userService.getUserInfoByToken(request))
    }

}