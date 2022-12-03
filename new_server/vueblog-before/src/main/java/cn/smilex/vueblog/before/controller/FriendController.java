package cn.smilex.vueblog.before.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.service.FriendService;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.PathPrefix;
import com.linecorp.armeria.server.annotation.ProducesJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author smilex
 * @date 2022/11/19/9:46
 * @since 1.0
 */
@SuppressWarnings("unused")
@PathPrefix("/friend")
@CrossOrigin
@Component
public class FriendController {
    private FriendService friendService;

    @Autowired
    public void setFriendService(FriendService friendService) {
        this.friendService = friendService;
    }

    /**
     * 查询所有友链
     *
     * @return 所有友链
     */
    @Get("/list")
    @ProducesJson
    public Result<?> list() {
        return Result.success(friendService.selectAllFriend());
    }
}
