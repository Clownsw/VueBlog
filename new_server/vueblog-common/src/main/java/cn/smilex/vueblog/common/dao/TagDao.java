package cn.smilex.vueblog.common.dao;

import cn.smilex.vueblog.common.entity.tag.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author smilex
 * @date 2022/11/12/11:42
 * @since 1.0
 */
@SuppressWarnings("unused")
@Mapper
public interface TagDao extends BaseMapper<Tag> {

    /**
     * 根据博文ID查询所有标签
     *
     * @param blogId 博文ID
     * @return 所有标签
     */
    List<Tag> selectTagByBlogId(@Param("blogId") Long blogId);

    /**
     * 批量删除博文标签
     *
     * @param blogId    博文ID
     * @param tagIdList 标签ID集合
     * @return 影响行数
     */
    int batchRemoveBlogTag(
            @Param("blogId") Long blogId,
            @Param("tagIdList") String tagIdList
    );

    /**
     * 批量添加博文标签
     *
     * @param values values
     * @return 影响行数
     */
    int batchAddBlogTag(@Param("values") String values);

    /**
     * 批量更新博文标签
     *
     * @param values    值
     * @param blogId    博文ID
     * @param tagIdList 标签ID集合
     * @return 影响行数
     */
    int batchUpdateBlogTag(
            @Param("values") String values,
            @Param("blogId") Long blogId,
            @Param("tagIdList") String tagIdList
    );
}
