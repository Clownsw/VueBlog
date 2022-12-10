package cn.smilex.vueblog.after.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.entity.blog.RequestBlog;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.handler.AuthService;
import cn.smilex.vueblog.common.service.BlogService;
import com.linecorp.armeria.server.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author smilex
 * @date 2022/12/10/12:56
 */
@SuppressWarnings("unused")
@Slf4j
@PathPrefix("/blog")
@ProducesJson
@RequestConverter(JacksonRequestConverterFunction.class)
@Decorator(AuthService.class)
@CrossOrigin
@Component
public class BlogController {
    private BlogService blogService;

    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
    }

    /**
     * 分页查询博文列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 博文列表
     */
    @Get("/list")
    @Options("/list")
    public Result<?> list(
            @Param("currentPage") Long currentPage,
            @Param("pageSize") Long pageSize
    ) {
        return Result.success(blogService.selectBlogPage(currentPage, pageSize));
    }

    /**
     * 添加或编辑博文
     *
     * @param requestBlog 请求博文对象
     * @return 结果
     */
    @Post("/edit")
    @Options("/edit")
    public Result<?> edit(@RequestObject RequestBlog requestBlog) {
        return blogService.edit(requestBlog) ? Result.success() : Result.error();
    }
}
