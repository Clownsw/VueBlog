package cn.smilex.vueblog.common.service;

import cn.smilex.vueblog.common.entity.blog.Blog;
import cn.smilex.vueblog.common.entity.blog.BlogKey;
import cn.smilex.vueblog.common.entity.blog.RequestBlog;
import cn.smilex.vueblog.common.entity.blog.SelectShowBlog;
import cn.smilex.vueblog.common.entity.common.Limit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
     * @return 博文集合
     */
    Limit<SelectShowBlog> selectBlogPage(Long currentPage);

    /**
     * 分页查询博文
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 博文集合
     */
    Limit<SelectShowBlog> selectBlogPage(
            Long currentPage,
            Long pageSize
    );

    /**
     * 根据ID获取文章信息
     *
     * @param id id
     * @return 文章信息
     */
    SelectShowBlog selectSelectShowBlogById(Long id, boolean isAdmin);

    /**
     * 根据ID查询博文
     *
     * @param id ID
     * @return 博文
     */
    Blog selectBlogById(Long id);

    /**
     * 根据指定标签分页查询所有文章
     *
     * @param currentPage 当前页
     * @param tagId       标签ID
     * @return 指定标签下的所有文章
     */
    Limit<SelectShowBlog> selectBlogPageByTagId(Long currentPage, Long tagId);

    /**
     * 分页查询指定分类下的所有文章
     *
     * @param currentPage 当前页
     * @param sortId      分类ID
     * @return 指定分类下的所有文章
     */
    Limit<SelectShowBlog> selectBlogPageBySortId(Long currentPage, Integer sortId);

    /**
     * 根据ID和秘钥查询文章信息
     *
     * @param id  id
     * @param key 秘钥
     * @return 文章信息
     */
    Blog selectBlogByIdAndKey(Long id, String key);

    /**
     * 分页查询博文列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 博文列表
     */
    Page<Blog> blogPage(Long currentPage, Long pageSize);

    /**
     * 添加或编辑博文
     *
     * @param requestBlog 请求博文对象
     * @return 结果
     */
    boolean edit(RequestBlog requestBlog);

    /**
     * 更新博文
     *
     * @param requestBlog 请求博文对象
     */
    void updateBlog(RequestBlog requestBlog);

    /**
     * 添加博文
     *
     * @param requestBlog 请求博文对象
     */
    void insertBlog(RequestBlog requestBlog);

    /**
     * 根据博文ID更新加密文章秘钥信息
     *
     * @param blogId   博文ID
     * @param key      秘钥
     * @param keyTitle 加密文章标题
     * @return 是否成功
     */
    boolean updateBlogKeyById(Long blogId, String key, String keyTitle);

    /**
     * 根据ID批量删除
     *
     * @param idList ID集合
     */
    void batchRemove(List<Long> idList);

    /**
     * 根据id查询BlogKey
     *
     * @param id id
     * @return BlogKey
     */
    BlogKey selectBlogKeyById(Long id);
}
