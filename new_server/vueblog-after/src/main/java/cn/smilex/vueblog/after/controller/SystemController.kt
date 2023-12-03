package cn.smilex.vueblog.after.controller

import cn.smilex.vueblog.common.annotation.CrossOrigin
import cn.smilex.vueblog.common.config.ResultCode
import cn.smilex.vueblog.common.entity.common.Result
import cn.smilex.vueblog.common.entity.other.SystemUpdateRequest
import cn.smilex.vueblog.common.handler.AuthService
import cn.smilex.vueblog.common.service.SystemService
import cn.smilex.vueblog.common.util.ClassUtils
import cn.smilex.vueblog.common.util.CommonUtils
import com.linecorp.armeria.server.annotation.*
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author smilex
 * @date 2023/12/3 17:41:34
 */
@Suppress("unused")
@PathPrefix("/system")
@Decorator(AuthService::class)
@RequestConverter(JacksonRequestConverterFunction::class)
@ProducesJson
@CrossOrigin
@Component
class SystemController(private val systemService: SystemService) {
    /**
     * 获取系统信息
     *
     * @return 系统信息
     */
    @Get("/info")
    @Options("/info")
    fun systemInfo(): Result<*> {
        return Result.success(
            systemService.selectSystemInfo()
        )
    }

    /**
     * 更新系统设置
     *
     * @param systemUpdateRequest 系统设置更新请求对象
     * @return 结果
     */
    @Post("/update")
    @Options("/update")
    fun update(@RequestObject systemUpdateRequest: Optional<SystemUpdateRequest>): Result<*> {
        if (CommonUtils.isEmpty(systemUpdateRequest) || ClassUtils.objIsNull(
                SystemUpdateRequest::class.java, systemUpdateRequest.get()
            )
        ) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR)
        }
        systemService.updateSystem(systemUpdateRequest.get())
        return Result.success()
    }
}