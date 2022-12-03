package cn.smilex.vueblog.common.dao;

import cn.smilex.vueblog.common.entity.tag.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author smilex
 * @date 2022/11/12/11:42
 * @since 1.0
 */
@SuppressWarnings("unused")
@Mapper
public interface TagDao extends BaseMapper<Tag> {
}
