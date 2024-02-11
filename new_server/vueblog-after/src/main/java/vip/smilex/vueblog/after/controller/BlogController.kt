package vip.smilex.vueblog.after.controller

import com.linecorp.armeria.server.annotation.*
import org.springframework.stereotype.Component
import vip.smilex.vueblog.common.annotation.CrossOrigin
import vip.smilex.vueblog.common.annotation.cache.Cache
import vip.smilex.vueblog.common.entity.blog.RequestBlog
import vip.smilex.vueblog.common.entity.common.Result
import vip.smilex.vueblog.common.handler.AuthService
import vip.smilex.vueblog.common.util.CommonUtils
import java.util.*

/**
 * @author smilex
 * @date 2023/12/3 17:48:50
 */
@Suppress("unused")
@PathPrefix("/blog")
@ProducesJson
@RequestConverter(JacksonRequestConverterFunction::class)
@Decorator(AuthService::class)
@CrossOrigin
@Component
class BlogController(private val blogService: vip.smilex.vueblog.common.service.BlogService) {

    /**
     * 分页查询博文列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 博文列表
     */
    @Post("/list")
    @Options("/list")
    @Cache(name = "aaa")
    fun list(
        @Param("currentPage") currentPage: Long?,
        @Param("pageSize") pageSize: Long?,
        @RequestObject queryString: Optional<String?>,
    ): Result<*> {
        return Result.success(
            blogService.selectBlogPage(
                currentPage,
                pageSize,
                CommonUtils.parseQueryStringToMap(queryString)
            )
        )
    }

    /**
     * 根据ID查询SelectShowBlog
     *
     * @param id ID
     * @return SelectShowBlog
     */
    @Get("/:id")
    @Options("/:id")
    fun info(@Param("id") id: Long?): Result<*> {
        return Result.success(blogService.selectSelectShowBlogById(id, true))
    }

    /**
     * 根据id查询BlogKey
     *
     * @param id id
     * @return BlogKey
     */
    @Get("/key/:id")
    @Options("/key/:id")
    fun blogKey(@Param("id") id: Long?): Result<*> {
        return Result.success(blogService.selectBlogKeyById(id))
    }

    /**
     * 添加或编辑博文
     *
     * @param requestBlog 请求博文对象
     * @return 结果
     */
    @Post("/edit")
    @Options("/edit")
    fun edit(@RequestObject requestBlog: Optional<RequestBlog?>): Result<*> {
        return requestBlog.filter { blog: RequestBlog? -> blogService.edit(blog) }
            .map { Result.success() }
            .orElseGet {
                Result.fromResultCode(
                    vip.smilex.vueblog.common.config.ResultCode.ERROR_REQUEST_PARAM_ERROR
                )
            }
    }

    /**
     * 根据ID集合批量删除博文
     *
     * @param idList ID集合
     * @return 结果
     */
    @Post("/batchDelete")
    @Options("/batchDelete")
    fun batchDelete(@RequestObject idList: Optional<List<Long?>>): Result<*> {
        return idList.filter { v: List<Long?> -> v.isNotEmpty() }
            .map { v: List<Long?>? ->
                blogService.batchRemove(v)
                Result.success()
            }
            .orElseGet {
                Result.fromResultCode(
                    vip.smilex.vueblog.common.config.ResultCode.ERROR_REQUEST_PARAM_ERROR
                )
            }
    }
}