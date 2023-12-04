package cn.smilex.vueblog.after.controller

import cn.smilex.vueblog.common.annotation.CrossOrigin
import cn.smilex.vueblog.common.config.ResultCode
import cn.smilex.vueblog.common.entity.common.Result
import cn.smilex.vueblog.common.entity.user.LoginUser
import cn.smilex.vueblog.common.function.TryRunExceptionHandler
import cn.smilex.vueblog.common.service.LoginService
import cn.smilex.vueblog.common.utils.ClassUtils
import cn.smilex.vueblog.common.utils.CommonUtils
import cn.smilex.vueblog.common.utils.JwtUtils
import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.server.annotation.*
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author smilex
 * @date 2023/12/3 17:46:55
 */
@Suppress("unused")
@CrossOrigin
@PathPrefix("/admin")
@RequestConverter(JacksonRequestConverterFunction::class)
@ProducesJson
@Component
class LoginController(private val loginService: LoginService) {

    /**
     * 登录用户
     *
     * @param loginUser 用户
     * @return token
     */
    @Post("/login")
    @Options("/login")
    fun login(loginUser: Optional<LoginUser?>, request: HttpRequest?): Result<*> {
        return if (!loginUser.isPresent || ClassUtils.objIsNull(LoginUser::class.java, loginUser.get())) {
            Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR)
        } else CommonUtils.tryRun(
            { loginService.login(loginUser.get(), request) },
            Optional.of(
                TryRunExceptionHandler { e: Throwable -> Optional.of(e) }
            )
        )
    }

    /**
     * 验证token有效性
     *
     * @param token token
     * @return 有效性
     */
    @Post("/token")
    @Options("/token")
    fun token(token: Optional<String?>): Result<*> {
        if (!token.isPresent || StringUtils.isBlank(token.get())) {
            return Result.error()
        }
        val result = JwtUtils.signJWTToken(token.get())
        return if (result.left == true) Result.success() else Result.error()
    }
}