package cn.smilex.vueblog.common.dao;

import cn.smilex.vueblog.common.entity.other.Friend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author smilex
 * @date 2022/11/12/11:43
 * @since 1.0
 */
@SuppressWarnings("unused")
@Mapper
public interface FriendDao extends BaseMapper<Friend> {
}
