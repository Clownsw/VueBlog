package cn.smilex.vueblog.after.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.entity.other.Friend;
import cn.smilex.vueblog.common.handler.AuthService;
import cn.smilex.vueblog.common.service.FriendService;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.linecorp.armeria.server.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author smilex
 * @date 2022/12/15/14:21
 */
@SuppressWarnings({"unused", "OptionalGetWithoutIsPresent"})
@Slf4j
@PathPrefix("/friend")
@ProducesJson
@RequestConverter(JacksonRequestConverterFunction.class)
@Decorator(AuthService.class)
@CrossOrigin
@Component
public class FriendController {
    private FriendService friendService;

    @Autowired
    public void setFriendService(FriendService friendService) {
        this.friendService = friendService;
    }

    /**
     * 分页查询友链列表
     *
     * @param currentPage 当前页
     * @return 友链列表
     */
    @Get("/list")
    @Options("/list")
    public Result<?> list(@Param("currentPage") Optional<Long> currentPage) {
        return Result.success(
                friendService.selectFriendPage(
                        currentPage.filter(v -> v > 0)
                                .orElse(1L)
                )
        );
    }


    /**
     * 添加一个友链
     *
     * @param friend 友链信息
     * @return 结果
     */
    @Post("/add")
    @Options("/add")
    public Result<?> add(@RequestObject Optional<Friend> friend) {
        if (CommonUtil.isEmpty(friend)) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR);
        }

        friendService.save(friend.get());
        return Result.success();
    }

    /**
     * 根据友链ID更新友链信息
     *
     * @param friend 友链信息
     * @return 结果
     */
    @Post("/update")
    @Options("/update")
    public Result<?> update(@RequestObject Optional<Friend> friend) {
        if (CommonUtil.isEmpty(friend)) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR);
        }

        friendService.updateById(friend.get());
        return Result.success();
    }

    /**
     * 根据ID集合批量删除友链
     *
     * @param idList ID集合
     * @return 结果
     */
    @Post("/batchDelete")
    @Options("/batchDelete")
    public Result<?> batchDelete(@RequestObject Optional<List<Long>> idList) {
        return idList.filter(v -> v.size() > 0)
                .<Result<?>>map(v -> {
                    friendService.batchDelete(v);
                    return Result.success();
                })
                .orElseGet(() -> Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR));
    }
}
