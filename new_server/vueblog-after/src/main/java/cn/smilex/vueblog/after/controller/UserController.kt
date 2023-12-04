package cn.smilex.vueblog.after.controller

import cn.smilex.vueblog.common.annotation.CrossOrigin
import cn.smilex.vueblog.common.entity.common.Result
import cn.smilex.vueblog.common.function.TryRunExceptionHandler
import cn.smilex.vueblog.common.handler.AuthService
import cn.smilex.vueblog.common.service.UserService
import cn.smilex.vueblog.common.utils.CommonUtils
import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.server.annotation.*
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author smilex
 * @date 2023/12/3 17:35:46
 */
@Suppress("unused")
@PathPrefix("/user")
@RequestConverter(JacksonRequestConverterFunction::class)
@Decorator(AuthService::class)
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
        return CommonUtils.tryRun(
            { userService.getUserInfoByToken(request) },
            Optional.of(
                TryRunExceptionHandler { e: Throwable -> Optional.of(e) }
            )
        )
    }

}