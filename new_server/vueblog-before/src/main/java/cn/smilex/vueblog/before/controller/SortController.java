package cn.smilex.vueblog.before.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.service.BlogService;
import cn.smilex.vueblog.common.service.SortService;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.PathPrefix;
import com.linecorp.armeria.server.annotation.ProducesJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author smilex
 * @date 2022/11/12/11:55
 * @since 1.0
 */
@SuppressWarnings("unused")
@PathPrefix("/sort")
@CrossOrigin
@Component
public class SortController {
    private SortService sortService;
    private BlogService blogService;

    @Autowired
    public void setSortService(SortService sortService) {
        this.sortService = sortService;
    }

    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
    }

    /**
     * 查询所有分类
     *
     * @return 所有分类
     */
    @Get("/list")
    @ProducesJson
    public Result<?> list() {
        return Result.success(sortService.selectSortList());
    }

    /**
     * 分页查询指定分类下的所有文章
     *
     * @param currentPage 当前页
     * @param sortId      分页ID
     * @return 指定分类下的所有文章
     */
    @Get("/blogList")
    @ProducesJson
    public Result<?> blogList(
            @Param("currentPage") Optional<Long> currentPage,
            @Param("sortId") Optional<Integer> sortId
    ) {
        return Result.success(blogService.selectBlogPageBySortId(currentPage.orElse(1L), sortId.orElse(1)));
    }
}
