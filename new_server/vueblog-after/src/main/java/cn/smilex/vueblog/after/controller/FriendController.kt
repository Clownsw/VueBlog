package cn.smilex.vueblog.after.controller

import cn.smilex.vueblog.common.annotation.CrossOrigin
import cn.smilex.vueblog.common.config.ResultCode
import cn.smilex.vueblog.common.entity.common.Result
import cn.smilex.vueblog.common.entity.other.Friend
import cn.smilex.vueblog.common.handler.AuthService
import cn.smilex.vueblog.common.service.FriendService
import cn.smilex.vueblog.common.utils.CommonUtils
import com.linecorp.armeria.server.annotation.*
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author smilex
 * @date 2023/12/3 17:47:51
 */
@Suppress("unused")
@PathPrefix("/friend")
@ProducesJson
@RequestConverter(JacksonRequestConverterFunction::class)
@Decorator(AuthService::class)
@CrossOrigin
@Component
class FriendController(private val friendService: FriendService) {

    /**
     * 分页查询友链列表
     *
     * @param currentPage 当前页
     * @return 友链列表
     */
    @Get("/list")
    @Options("/list")
    fun list(@Param("currentPage") currentPage: Optional<Long>): Result<*> {
        return Result.success(
            friendService.selectFriendPage(
                currentPage.filter { v: Long -> v > 0 }
                    .orElse(1L)
            )
        )
    }


    /**
     * 添加一个友链
     *
     * @param friend 友链信息
     * @return 结果
     */
    @Post("/add")
    @Options("/add")
    fun add(@RequestObject friend: Optional<Friend>): Result<*> {
        if (CommonUtils.isEmpty(friend)) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR)
        }
        friendService.save(friend.get())
        return Result.success()
    }

    /**
     * 根据友链ID更新友链信息
     *
     * @param friend 友链信息
     * @return 结果
     */
    @Post("/update")
    @Options("/update")
    fun update(@RequestObject friend: Optional<Friend>): Result<*> {
        if (CommonUtils.isEmpty(friend)) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR)
        }
        friendService.updateById(friend.get())
        return Result.success()
    }

    /**
     * 根据ID集合批量删除友链
     *
     * @param idList ID集合
     * @return 结果
     */
    @Post("/batchDelete")
    @Options("/batchDelete")
    fun batchDelete(@RequestObject idList: Optional<List<Long?>>): Result<*> {
        return idList.filter { v: List<Long?> -> v.isNotEmpty() }
            .map { v: List<Long?>? ->
                friendService.batchDelete(v)
                Result.success()
            }
            .orElseGet {
                Result.fromResultCode(
                    ResultCode.ERROR_REQUEST_PARAM_ERROR
                )
            }
    }
}