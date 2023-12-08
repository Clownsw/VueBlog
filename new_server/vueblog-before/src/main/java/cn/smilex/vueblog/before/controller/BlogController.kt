package cn.smilex.vueblog.before.controller

import cn.smilex.vueblog.common.annotation.CrossOrigin
import cn.smilex.vueblog.common.config.ResultCode
import cn.smilex.vueblog.common.entity.common.Result
import cn.smilex.vueblog.common.service.BlogService
import cn.smilex.vueblog.common.util.CommonUtils
import cn.smilex.vueblog.common.util.OptionUtils
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.PathPrefix
import com.linecorp.armeria.server.annotation.ProducesJson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

/**
 *
 * @author smilex
 * @date 2023/12/3 16:01:27
 */
@Suppress("unused")
@PathPrefix("/blog")
@ProducesJson
@CrossOrigin
@Component
class BlogController(private val blogService: BlogService) {
    companion object {
        @JvmStatic
        val LOGGER: Logger = LoggerFactory.getLogger(BlogController::class.java)
    }

    /**
     * 分页查询博文
     *
     * @param currentPage 当前页
     * @return 博文集合
     */
    @Get("/list")
    fun list(
        @Param("currentPage") currentPage: Optional<Long>
    ): Result<*> {
        return Result.success(
            blogService.selectBlogPage(currentPage.orElse(1L))
        )
    }

    /**
     * 根据ID获取文章信息
     *
     * @param id      id
     * @return 文章信息
     */
    @Get("/:id")
    fun selectBlogById(
        @Param("id") id: Long
    ): Result<*> {
        return Result.success(
            blogService.selectSelectShowBlogById(id, false)
        )
    }

    /**
     * 根据ID和秘钥查询文章信息
     *
     * @param id  id
     * @param key 秘钥
     * @return 文章信息
     */
    @Get("/key/:id/:key")
    fun selectBlogByIdAndKey(
        @Param("id") id: Optional<Long>,
        @Param("key") key: Optional<String>
    ): Result<*> {

        if (CommonUtils.objectListCheckHaveNull(id, key)) {
            return Result.error(ResultCode.ERROR_REQUEST_PARAM_ERROR)
        }

        return OptionUtils.isEmptyAction(
            key,
            { q1 ->
                OptionUtils.isEmpty(q1)
            },
            {
                Result.error(ResultCode.ERROR_REQUEST_PARAM_ERROR)
            },
            { q1 ->
                Result.success(blogService.selectBlogByIdAndKey(id.get(), q1))
            }
        )
    }
}