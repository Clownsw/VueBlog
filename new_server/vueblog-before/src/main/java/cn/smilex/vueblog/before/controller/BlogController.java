package cn.smilex.vueblog.before.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.config.CommonConfig;
import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.service.BlogService;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.PathPrefix;
import com.linecorp.armeria.server.annotation.ProducesJson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author smilex
 * @date 2022/11/12/11:52
 * @since 1.0
 */
@SuppressWarnings("all")
@CrossOrigin
@PathPrefix("/blog")
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
            @Param("currentPage") Optional<Long> currentPage
    ) {
        return Result.success(blogService.selectBlogPage(currentPage.orElse(1L)));
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
            @Param("id") Long id
    ) {
        return Result.success(blogService.selectSelectShowBlogById(id));
    }

    /**
     * 根据ID和秘钥查询文章信息
     *
     * @param id  id
     * @param key 秘钥
     * @return 文章信息
     */
    @Get("/key/:id/:key")
    @ProducesJson
    public Result<?> selectBlogByIdAndKey(
            @Param("id") Optional<Long> id,
            @Param("key") Optional<String> key
    ) {

        if (CommonUtil.objectListCheckHaveNull(id, key)) {
            return Result.error(ResultCode.ERROR_REQUEST_PARAM_ERROR);
        }

        final String _key = key.get();
        if (StringUtils.isBlank(_key) || _key.length() < 0 || _key.length() > CommonConfig.VUEBLOG_BLOG_KEY_SIZE) {
            return Result.error(ResultCode.ERROR_REQUEST_PARAM_ERROR);
        }

        return Result.success(blogService.selectBlogByIdAndKey(id.get(), _key));
    }
}
