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

import java.util.List;
import java.util.Optional;

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
     * 根据ID查询SelectShowBlog
     *
     * @param id ID
     * @return SelectShowBlog
     */
    @Get("/:id")
    @Options("/:id")
    public Result<?> info(@Param("id") Long id) {
        return Result.success(blogService.selectSelectShowBlogById(id));
    }

    /**
     * 添加或编辑博文
     *
     * @param requestBlog 请求博文对象
     * @return 结果
     */
    @Post("/edit")
    @Options("/edit")
    public Result<?> edit(@RequestObject Optional<RequestBlog> requestBlog) {
        return requestBlog.filter(blog -> blogService.edit(blog)).<Result<?>>map(blog -> Result.success())
                .orElseGet(Result::error);
    }

    /**
     * 根据ID集合批量删除博文
     *
     * @param idList ID集合
     * @return 结果
     */
    @Post("/batchDelete")
    @Options("/batchDelete")
    public Result<?> batchDelete(@RequestObject Optional<List<Long>> idList) {
        return idList.filter(v -> v.size() > 0)
                .<Result<?>>map(v -> {
                    blogService.batchRemove(v);
                    return Result.success();
                })
                .orElseGet(Result::error);
    }
}
