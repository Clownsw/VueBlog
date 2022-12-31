package cn.smilex.vueblog.common.dao;

import cn.smilex.vueblog.common.entity.blog.Blog;
import cn.smilex.vueblog.common.entity.blog.BlogKey;
import cn.smilex.vueblog.common.entity.blog.SelectBlogInfo;
import cn.smilex.vueblog.common.entity.blog.SelectShowBlog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author smilex
 * @date 2022/11/12/11:41
 * @since 1.0
 */
@SuppressWarnings("unused")
@Mapper
public interface BlogDao extends BaseMapper<Blog> {

    /**
     * 分页查询博文
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 博文集合
     */
    List<SelectShowBlog> selectBlogPage(
            @Param("pageSize") Long pageSize,
            @Param("currentPage") Long currentPage
    );

    /**
     * 根据指定标签分页查询所有文章
     *
     * @param currentPage 当前页
     * @param pageSize    分页大小
     * @param tagId       标签ID
     * @return 博文集合
     */
    List<SelectShowBlog> selectBlogPageByTagId(
            @Param("pageSize") Long pageSize,
            @Param("currentPage") Long currentPage,
            @Param("tagId") Long tagId
    );

    /**
     * 根据指定分类分页查询所有文章
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @param sortId      分类ID
     * @return 博文集合
     */
    List<SelectShowBlog> selectBlogPageBySortId(
            @Param("pageSize") Long pageSize,
            @Param("currentPage") Long currentPage,
            @Param("sortId") Integer sortId
    );

    /**
     * 根据ID获取文章信息
     *
     * @param id id
     * @return 文章信息
     */
    SelectBlogInfo selectBlogById(
            @Param("id") Long id,
            @Param("isAdmin") boolean isAdmin
    );

    /**
     * 根据ID获取文章信息 (包含content)
     *
     * @param id id
     * @return 文章信息
     */
    SelectBlogInfo selectShowBlogById(Long id);

    /**
     * 根据标签ID查询文章个数
     *
     * @param tagId 标签ID
     * @return 文章个数
     */
    long selectBlogCountByTagId(@Param("tagId") Long tagId);

    /**
     * 根据分类ID查询文章个数
     *
     * @param sortId 分类ID
     * @return 文章个数
     */
    long selectBlogCountBySortId(@Param("sortId") Integer sortId);

    /**
     * 根据ID和秘钥查询文章信息
     *
     * @param id  id
     * @param key 秘钥
     * @return 文章信息
     */
    Blog selectBlogByIdAndKey(
            @Param("id") Long id,
            @Param("key") String key
    );

    /**
     * 根据博文ID更新加密文章秘钥信息
     *
     * @param blogId   博文ID
     * @param key      秘钥
     * @param keyTitle 加密文章标题
     * @return 是否成功
     */
    int updateBlogKeyById(
            @Param("blogId") Long blogId,
            @Param("key") String key,
            @Param("keyTitle") String keyTitle
    );

    /**
     * 根据id查询BlogKey
     *
     * @param id id
     * @return BlogKey
     */
    BlogKey selectBlogKeyById(@Param("id") Long id);
}
