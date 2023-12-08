package cn.smilex.vueblog.before.controller

import cn.smilex.vueblog.common.annotation.CrossOrigin
import cn.smilex.vueblog.common.entity.common.Result
import cn.smilex.vueblog.common.service.FriendService
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.PathPrefix
import com.linecorp.armeria.server.annotation.ProducesJson
import org.springframework.stereotype.Component

/**
 * @author smilex
 * @date 2023/12/3 17:16:22
 */
@Suppress("unused")
@PathPrefix("/friend")
@ProducesJson
@CrossOrigin
@Component
class FriendController(private val friendService: FriendService) {

    /**
     * 查询所有友链
     *
     * @return 所有友链
     */
    @Get("/list")
    fun list(): Result<*>? {
        return Result.success(friendService.selectAllFriend())
    }

}