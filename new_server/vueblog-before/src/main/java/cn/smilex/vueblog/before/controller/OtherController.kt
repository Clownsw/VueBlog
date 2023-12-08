package cn.smilex.vueblog.before.controller

import cn.smilex.vueblog.common.annotation.CrossOrigin
import cn.smilex.vueblog.common.entity.common.Result
import cn.smilex.vueblog.common.service.OtherService
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.PathPrefix
import com.linecorp.armeria.server.annotation.ProducesJson
import org.springframework.stereotype.Component

/**
 * @author smilex
 * @date 2023/12/3 17:21:10
 */
@Suppress("unused")
@PathPrefix("/other")
@ProducesJson
@CrossOrigin
@Component
class OtherController(private val otherService: OtherService) {
    /**
     * 查询页面底部脚本
     *
     * @return 页面底部脚本
     */
    @Get("/footer")
    fun footer(): Result<*> {
        return Result.success(otherService.selectPageFooter())
    }

    /**
     * 获取关于我信息
     *
     * @return 关于我信息
     */
    @Get("/me")
    fun aboutMe(): Result<*> {
        return Result.success(otherService.aboutMe())
    }
}