package cn.smilex.vueblog.after.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.handler.AuthService;
import cn.smilex.vueblog.common.service.BlogService;
import com.linecorp.armeria.server.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author smilex
 * @date 2022/12/4/16:24
 * @since 1.0
 */
@SuppressWarnings("unused")
@Slf4j
@PathPrefix("/blog")
@ProducesJson
@Decorator(AuthService.class)
@RequestConverter(JacksonRequestConverterFunction.class)
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
    @Options("/page")
    @Get("/page")
    @ProducesJson
    public Result<?> page(
            @Param("currentPage") Optional<Long> currentPage,
            @Param("pageSize") Optional<Long> pageSize
    ) {
        return Result.success(
                blogService.blogPage(
                        currentPage.orElse(1L),
                        pageSize.orElse(10L)
                )
        );
    }
}
