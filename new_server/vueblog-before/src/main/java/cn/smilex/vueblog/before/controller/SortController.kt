package cn.smilex.vueblog.before.controller

import cn.smilex.vueblog.common.annotation.CrossOrigin
import cn.smilex.vueblog.common.entity.common.Result
import cn.smilex.vueblog.common.service.BlogService
import cn.smilex.vueblog.common.service.SortService
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.PathPrefix
import com.linecorp.armeria.server.annotation.ProducesJson
import org.springframework.stereotype.Component
import java.util.*

/**
 * TODO
 * @author smilex
 * @date 2023/12/3 17:24:53
 */
@Suppress("unused")
@PathPrefix("/sort")
@ProducesJson
@CrossOrigin
@Component
class SortController(
    private val sortService: SortService,
    private val blogService: BlogService
) {
    /**
     * 查询所有分类
     *
     * @return 所有分类
     */
    @Get("/list")
    fun list(): Result<*> {
        return Result.success(sortService.selectSortList())
    }

    /**
     * 分页查询指定分类下的所有文章
     *
     * @param currentPage 当前页
     * @param sortId      分页ID
     * @return 指定分类下的所有文章
     */
    @Get("/blogList")
    fun blogList(
        @Param("currentPage") currentPage: Optional<Long?>,
        @Param("sortId") sortId: Optional<Int?>
    ): Result<*> {
        return Result.success(
            blogService.selectBlogPageBySortId(currentPage.orElse(1L), sortId.orElse(1))
        )
    }
}