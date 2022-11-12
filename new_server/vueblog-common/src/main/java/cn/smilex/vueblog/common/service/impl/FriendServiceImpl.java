package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.FriendDao;
import cn.smilex.vueblog.common.entity.Friend;
import cn.smilex.vueblog.common.service.FriendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author smilex
 * @date 2022/11/12/11:48
 * @since 1.0
 */
@SuppressWarnings("unused")
@Service
public class FriendServiceImpl extends ServiceImpl<FriendDao, Friend> implements FriendService {
}
