package cn.smilex.vueblog.common.dao

import cn.smilex.vueblog.common.entity.blog.Blog
import cn.smilex.vueblog.common.entity.blog.BlogKey
import cn.smilex.vueblog.common.entity.blog.SelectBlogInfo
import cn.smilex.vueblog.common.entity.blog.SelectShowBlog
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Param

/**
 * TODO
 * @author smilex
 * @date 2023/12/4 21:38:16
 */
interface BlogDao : BaseMapper<Blog> {
    /**
     * 分页查询博文
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 博文集合
     */
    fun selectBlogPage(
        @Param("pageSize") pageSize: Long?,
        @Param("currentPage") currentPage: Long?,
        @Param("sql") sql: String?
    ): List<SelectShowBlog?>?

    /**
     * 根据指定标签分页查询所有文章
     *
     * @param currentPage 当前页
     * @param pageSize    分页大小
     * @param tagId       标签ID
     * @return 博文集合
     */
    fun selectBlogPageByTagId(
        @Param("pageSize") pageSize: Long?,
        @Param("currentPage") currentPage: Long?,
        @Param("tagId") tagId: Long?
    ): List<SelectShowBlog?>?

    /**
     * 根据指定分类分页查询所有文章
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @param sortId      分类ID
     * @return 博文集合
     */
    fun selectBlogPageBySortId(
        @Param("pageSize") pageSize: Long?,
        @Param("currentPage") currentPage: Long?,
        @Param("sortId") sortId: Int?
    ): List<SelectShowBlog?>?

    /**
     * 根据ID获取文章信息
     *
     * @param id id
     * @return 文章信息
     */
    fun selectBlogById(
        @Param("id") id: Long?,
        @Param("isAdmin") isAdmin: Boolean
    ): SelectBlogInfo?

    /**
     * 根据ID获取文章信息 (包含content)
     *
     * @param id id
     * @return 文章信息
     */
    fun selectShowBlogById(id: Long?): SelectBlogInfo?

    /**
     * 根据标签ID查询文章个数
     *
     * @param tagId 标签ID
     * @return 文章个数
     */
    fun selectBlogCountByTagId(@Param("tagId") tagId: Long?): Long

    /**
     * 根据分类ID查询文章个数
     *
     * @param sortId 分类ID
     * @return 文章个数
     */
    fun selectBlogCountBySortId(@Param("sortId") sortId: Int?): Long

    /**
     * 根据ID和秘钥查询文章信息
     *
     * @param id  id
     * @param key 秘钥
     * @return 文章信息
     */
    fun selectBlogByIdAndKey(
        @Param("id") id: Long?,
        @Param("key") key: String?
    ): Blog?

    /**
     * 根据博文ID更新加密文章秘钥信息
     *
     * @param blogId   博文ID
     * @param key      秘钥
     * @param keyTitle 加密文章标题
     * @return 是否成功
     */
    fun updateBlogKeyById(
        @Param("blogId") blogId: Long?,
        @Param("key") key: String?,
        @Param("keyTitle") keyTitle: String?
    ): Int

    /**
     * 根据id查询BlogKey
     *
     * @param id id
     * @return BlogKey
     */
    fun selectBlogKeyById(@Param("id") id: Long?): BlogKey?

    /**
     * 查询count根据自定义sql
     *
     * @param sql sql
     * @return count
     */
    fun selectCountByCustomSql(@Param("sql") sql: String?): Long
}