package vip.smilex.vueblog.after.controller

import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.server.annotation.*
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import vip.smilex.vueblog.common.annotation.CrossOrigin
import vip.smilex.vueblog.common.entity.common.Result
import vip.smilex.vueblog.common.util.JwtUtils
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
class LoginController(private val loginService: vip.smilex.vueblog.common.service.LoginService) {

    /**
     * 登录用户
     *
     * @param loginUser 用户
     * @return token
     */
    @Post("/login")
    @Options("/login")
    fun login(
        loginUser: Optional<vip.smilex.vueblog.common.entity.user.LoginUser?>,
        request: HttpRequest?,
    ): Result<*> {
        if (!loginUser.isPresent || vip.smilex.vueblog.common.util.ClassUtils.objIsNull(vip.smilex.vueblog.common.entity.user.LoginUser::class.java,
                loginUser.get())
        ) {
            return Result.fromResultCode(vip.smilex.vueblog.common.config.ResultCode.ERROR_REQUEST_PARAM_ERROR)
        }

        return Result.success(loginService.login(loginUser.get(), request))
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