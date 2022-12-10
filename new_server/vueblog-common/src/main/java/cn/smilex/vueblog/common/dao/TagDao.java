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
}
