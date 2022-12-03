package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.FriendDao;
import cn.smilex.vueblog.common.entity.other.Friend;
import cn.smilex.vueblog.common.entity.common.VueBlogConfig;
import cn.smilex.vueblog.common.service.FriendService;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                            CommonUtil.EMPTY_FRIEND_MESSAGE,
                            CommonUtil.EMPTY_FRIEND_MESSAGE,
                            vueBlogConfig.getVueBlogFriendDefaultHref(),
                            vueBlogConfig.getVueBlogFriendDefaultAvatar()
                    )
            );
        }

        return friendList;
    }
}
