package cn.smilex.vueblog.before.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.service.BlogService;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.PathPrefix;
import com.linecorp.armeria.server.annotation.ProducesJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author smilex
 * @date 2022/11/14/21:05
 * @since 1.0
 */
@SuppressWarnings("unused")
@PathPrefix("/tag")
@ProducesJson
@CrossOrigin
@Component
public class TagController {
    private BlogService blogService;

    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
    }

    /**
     * 分页查询指定标签下的所有文章
     *
     * @param currentPage 当前页
     * @param tagId       标签ID
     * @return 指定标签下的所有文章
     */
    @Get("/blogList")
    public Result<?> blogList(
            @Param("currentPage") Optional<Long> currentPage,
            @Param("tagId") Optional<Long> tagId
    ) {
        return Result.success(blogService.selectBlogPageByTagId(currentPage.orElse(0L), tagId.orElse(0L)));
    }
}
