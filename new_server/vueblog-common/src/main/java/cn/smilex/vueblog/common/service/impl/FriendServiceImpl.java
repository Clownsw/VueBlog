package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.config.CommonConfig;
import cn.smilex.vueblog.common.dao.FriendDao;
import cn.smilex.vueblog.common.entity.common.Limit;
import cn.smilex.vueblog.common.entity.common.VueBlogConfig;
import cn.smilex.vueblog.common.entity.other.Friend;
import cn.smilex.vueblog.common.service.FriendService;
import cn.smilex.vueblog.common.util.CommonUtil;
import cn.smilex.vueblog.common.util.StructuredTaskScope;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author smilex
 * @date 2022/11/12/11:48
 * @since 1.0
 */
@SuppressWarnings("unused")
@Service
public class FriendServiceImpl extends ServiceImpl<FriendDao, Friend> implements FriendService {

    private VueBlogConfig vueBlogConfig;

    @Autowired
    public void setVueBlogConfig(VueBlogConfig vueBlogConfig) {
        this.vueBlogConfig = vueBlogConfig;
    }

    /**
     * 查询所有友链
     *
     * @return 所有友链
     */
    @Override
    public List<Friend> selectAllFriend() {
        List<Friend> friendList = this.list();

        if (friendList.size() == 0) {
            friendList.add(
                    new Friend(
                            0L,
                            CommonConfig.EMPTY_FRIEND_MESSAGE,
                            CommonConfig.EMPTY_FRIEND_MESSAGE,
                            vueBlogConfig.getVueBlogFriendDefaultHref(),
                            vueBlogConfig.getVueBlogFriendDefaultAvatar()
                    )
            );
        }

        return friendList;
    }

    /**
     * 分页查询友链列表
     *
     * @param currentPage 当前页
     * @return 友链列表
     */
    @Override
    public Limit<Friend> selectFriendPage(Long currentPage) {
        Limit<Friend> limit = Limit.defaultLimit(
                vueBlogConfig.getVueBlogAfterFriendPageSize(),
                currentPage
        );

        try (StructuredTaskScope scope = new StructuredTaskScope(2)) {
            scope.execute(() -> limit.setDataList(
                    this.list(
                            new QueryWrapper<Friend>()
                                    .last(
                                            String.format(
                                                    "LIMIT %d, %d",
                                                    CommonUtil.calcLimit(
                                                            currentPage,
                                                            vueBlogConfig.getVueBlogAfterFriendPageSize()
                                                    ),
                                                    vueBlogConfig.getVueBlogAfterFriendPageSize()
                                            )
                                    )
                    )
            ));

            scope.execute(() -> {
                limit.setTotalCount(this.count());
                limit.setPageCount(
                        CommonUtil.calcPageCount(
                                limit.getTotalCount(),
                                vueBlogConfig.getVueBlogAfterFriendPageSize()
                        )
                );
            });
        }

        return limit;
    }

    /**
     * 根据ID集合批量删除友链
     *
     * @param idList ID集合
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<Long> idList) {
        this.getBaseMapper()
                .batchDelete(
                        CommonUtil.collectionToStr(
                                idList,
                                Object::toString,
                                ","
                        )
                );
    }
}
