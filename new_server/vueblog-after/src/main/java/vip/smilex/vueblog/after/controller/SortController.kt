package vip.smilex.vueblog.after.controller

import com.linecorp.armeria.server.annotation.*
import org.springframework.stereotype.Component
import vip.smilex.vueblog.common.annotation.CrossOrigin
import vip.smilex.vueblog.common.config.ResultCode
import vip.smilex.vueblog.common.entity.common.Result
import vip.smilex.vueblog.common.entity.sort.Sort
import vip.smilex.vueblog.common.service.SortService
import vip.smilex.vueblog.common.util.ClassUtils
import vip.smilex.vueblog.common.util.CommonUtils
import java.util.*

/**
 * @author smilex
 * @date 2023/12/3 17:42:43
 */
@Suppress("unused")
@PathPrefix("/sort")
@ProducesJson
@Decorator(vip.smilex.vueblog.common.handler.AuthService::class)
@RequestConverter(JacksonRequestConverterFunction::class)
@CrossOrigin
@Component
class SortController(private val sortService: SortService) {
    /**
     * 查询所有分类
     *
     * @return 所有分类
     */
    @Get("/list")
    @Options("/list")
    fun list(): Result<*>? {
        return Result.success(sortService.selectSortList())
    }

    /**
     * 添加一个分类
     *
     * @return 结果
     */
    @Post("/add")
    @Options("/add")
    fun add(@RequestObject sort: Optional<Sort>): Result<*> {
        if (CommonUtils.isEmpty(sort) || ClassUtils.objIsNull(
                Sort::class.java, sort.get(), "id"
            )
        ) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR)
        }
        sortService.save(sort.get())
        return Result.success()
    }

    /**
     * 根据分类ID更新分类信息
     *
     * @param sort 分类信息
     * @return 结果
     */
    @Post("/update")
    @Options("/update")
    fun update(@RequestObject sort: Optional<Sort>): Result<*> {
        if (CommonUtils.isEmpty(sort) || ClassUtils.objIsNull(
                Sort::class.java, sort.get()
            )
        ) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR)
        }
        sortService.updateById(sort.get())
        return Result.success()
    }

    /**
     * 根据分类ID删除分类
     *
     * @param sortId 分类ID
     * @return 结果
     */
    @Get("/delete/:sortId")
    @Options("/delete/:sortId")
    fun delete(@Param("sortId") sortId: Int): Result<*> {
        if (sortId > 0) {
            sortService.removeById(sortId)
        }
        return Result.success()
    }
}