package cn.smilex.vueblog.before.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.entity.Result;
import cn.smilex.vueblog.common.handler.AuthService;
import cn.smilex.vueblog.common.service.BlogService;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.server.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author smilex
 * @date 2022/11/12/11:52
 * @since 1.0
 */
@SuppressWarnings("unused")
@CrossOrigin
@PathPrefix("/blog")
@Decorator(AuthService.class)
@Component
public class BlogController {
    private BlogService blogService;

    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
    }

    /**
     * 分页查询博文
     *
     * @param currentPage 当前页
     * @param request     请求对象
     * @return 博文集合
     */
    @Get("/list")
    @ProducesJson
    public Result<?> list(
            @Param("currentPage") Optional<Long> currentPage,
            HttpRequest request
    ) {
        return Result.success(blogService.selectBlogPage(currentPage, request));
    }

    /**
     * 根据ID获取文章信息
     *
     * @param id      id
     * @param request 请求对象
     * @return 文章信息
     */
    @Get("/:id")
    @ProducesJson
    public Result<?> selectBlogById(
            @Param("id") Long id,
            HttpRequest request
    ) {
        return Result.success(blogService.selectBlogById(id, request));
    }
}
