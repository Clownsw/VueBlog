package cn.smilex.vueblog.before.controller

import cn.smilex.vueblog.common.annotation.CrossOrigin
import cn.smilex.vueblog.common.entity.common.Result
import cn.smilex.vueblog.common.service.SystemService
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.PathPrefix
import com.linecorp.armeria.server.annotation.ProducesJson
import org.springframework.stereotype.Component

/**
 * @author smilex
 * @date 2023/12/3 17:25:59
 */
@Suppress("unused")
@PathPrefix("/system")
@ProducesJson
@CrossOrigin
@Component
class SystemController(private val systemService: SystemService) {

    /**
     * 查询系统信息
     *
     * @return 系统信息
     */
    @Get("/info")
    fun info(): Result<*>? {
        return Result.success(systemService.selectSystemInfo())
    }

}