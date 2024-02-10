package vip.smilex.vueblog.after.controller

import com.linecorp.armeria.server.annotation.*
import org.springframework.stereotype.Component
import vip.smilex.vueblog.common.annotation.CrossOrigin
import vip.smilex.vueblog.common.config.ResultCode
import vip.smilex.vueblog.common.entity.common.Result
import vip.smilex.vueblog.common.entity.other.AboutMe
import vip.smilex.vueblog.common.entity.other.BackUp
import vip.smilex.vueblog.common.handler.AuthService
import vip.smilex.vueblog.common.service.OtherService
import vip.smilex.vueblog.common.util.ClassUtils
import vip.smilex.vueblog.common.util.CommonUtils
import java.util.*

/**
 * @author smilex
 * @date 2023/12/3 17:44:07
 */
@Suppress("unused")
@PathPrefix("/other")
@ProducesJson
@RequestConverter(JacksonRequestConverterFunction::class)
@CrossOrigin
@Decorator(AuthService::class)
@Component
class OtherController(private val otherService: OtherService) {

    /**
     * 获取关于我信息
     *
     * @return 关于我信息
     */
    @Get("/me")
    @Options("/me")
    fun aboutMe(): Result<*> {
        return Result.success(otherService.aboutMe())
    }

    /**
     * 更新关于我
     *
     * @param aboutMe 关于我
     * @return 结果
     */
    @Post("/me/update")
    @Options("/me/update")
    fun update(@RequestObject aboutMe: Optional<AboutMe>): Result<*> {
        return aboutMe.filter { v: AboutMe ->
            try {
                return@filter !ClassUtils.objIsNull(AboutMe::class.java, v, "id")
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
            .map { v: AboutMe? ->
                otherService.updateMe(v)
                Result.success()
            }
            .orElseGet {
                Result.fromResultCode(
                    ResultCode.ERROR_REQUEST_PARAM_ERROR
                )
            }
    }

    /**
     * 查询备份信息
     *
     * @return 备份信息
     */
    @Get("/backUp/info")
    @Options("/backUp/info")
    fun backUpInfo(): Result<*> {
        return Result.success(otherService.selectBackUpInfo())
    }

    /**
     * 更新备份信息
     *
     * @return 结果
     */
    @Post("/backUp/update")
    @Options("/backUp/update")
    fun backUpUpdate(@RequestObject backUp: Optional<BackUp>): Result<*> {
        if (CommonUtils.isEmpty(backUp) || ClassUtils.objIsNull(BackUp::class.java, backUp.get())) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR)
        }
        otherService.updateBackUp(backUp.get())
        return Result.success()
    }

    /**
     * 备份数据库
     *
     * @return 结果
     */
    @Get("/backUp/buy")
    @Options("/backUp/buy")
    fun backUpBuy(): Result<*> {
        return otherService.backUpBuy()
    }

    /**
     * 查询页面底部脚本
     *
     * @return 页面底部脚本
     */
    @Get("/footer")
    @Options("/footer")
    fun footer(): Result<*> {
        return Result.success(otherService.selectPageFooter())
    }

    /**
     * 综合统计
     *
     * @return 统计数据
     */
    @Get("/statistics")
    @Options("/statistics")
    fun statistics(): Result<*> {
        return Result.success(otherService.statistics())
    }

    /**
     * 刷新搜索数据
     *
     * @return 结果
     */
    @Get("/flushSearchData")
    @Options("/flushSearchData")
    fun flushSearchData(): Result<*> {
        return if (otherService.flushSearchData()) Result.success() else Result.error()
    }
}