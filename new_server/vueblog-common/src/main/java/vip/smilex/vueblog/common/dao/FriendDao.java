package vip.smilex.vueblog.common.dao;

import vip.smilex.vueblog.common.entity.other.Friend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.smilex.vueblog.common.entity.other.Friend;

/**
 * @author smilex
 * @date 2022/11/12/11:43
 * @since 1.0
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
@Mapper
public interface FriendDao extends BaseMapper<Friend> {

    /**
     * 根据ID集合批量删除友链
     *
     * @param idList ID集合
     * @return 影响行数
     */
    long batchDelete(@Param("idList") String idList);
}
