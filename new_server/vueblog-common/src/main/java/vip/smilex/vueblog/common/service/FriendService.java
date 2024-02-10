package vip.smilex.vueblog.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.smilex.vueblog.common.entity.common.Limit;
import vip.smilex.vueblog.common.entity.other.Friend;

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

    /**
     * 根据ID集合批量删除友链
     *
     * @param idList ID集合
     */
    void batchDelete(List<Long> idList);
}
