package cn.smilex.vueblog.common.service;

import cn.smilex.vueblog.common.entity.Blog;
import cn.smilex.vueblog.common.entity.Limit;
import cn.smilex.vueblog.common.entity.SelectShowBlog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linecorp.armeria.common.HttpRequest;

import java.util.List;
import java.util.Optional;

/**
 * @author smilex
 * @date 2022/11/12/11:44
 * @since 1.0
 */
@SuppressWarnings("all")
public interface BlogService extends IService<Blog> {

    /**
     * 分页查询博文
     *
     * @param currentPage 当前页
     * @param request     请求对象
     * @return 博文集合
     */
    Limit<SelectShowBlog> selectBlogPage(
            Long currentPage,
            HttpRequest request
    );

    /**
     * 根据ID获取文章信息
     *
     * @param id      id
     * @param request 请求对象
     * @return 文章信息
     */
    SelectShowBlog selectBlogById(Long id, HttpRequest request);

    /**
     * 根据指定标签分页查询所有文章
     *
     * @param currentPage 当前页
     * @param tagId       标签ID
     * @return 指定标签下的所有文章
     */
    Limit<SelectShowBlog> selectBlogPageByTagId(Long currentPage, Long tagId);
}