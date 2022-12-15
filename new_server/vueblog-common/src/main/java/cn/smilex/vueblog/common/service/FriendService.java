package cn.smilex.vueblog.common.service;

import cn.smilex.vueblog.common.entity.common.Limit;
import cn.smilex.vueblog.common.entity.other.Friend;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author smilex
 * @date 2022/11/12/11:44
 * @since 1.0
 */
@SuppressWarnings("unused")
public interface FriendService extends IService<Friend> {

    /**
     * 查询所有友链
     *
     * @return 所有友链
     */
    List<Friend> selectAllFriend();

    /**
     * 分页查询友链列表
     *
     * @param currentPage 当前页
     * @return 友链列表
     */
    Limit<Friend> selectFriendPage(Long currentPage);
}
