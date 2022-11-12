package cn.smilex.vueblog.before.controller;

import cn.smilex.vueblog.common.entity.Result;
import cn.smilex.vueblog.common.service.BlogService;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.PathPrefix;
import com.linecorp.armeria.server.annotation.ProducesJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author smilex
 * @date 2022/11/12/11:52
 * @since 1.0
 */
@SuppressWarnings("unused")
@PathPrefix("/blog")
@Component
public class BlogController {
    private BlogService blogService;

    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
    }

    @Get("/selectAllBlog")
    @ProducesJson
    public Result<?> selectAllBlog() {
        return Result.success(blogService.list());
    }
}
