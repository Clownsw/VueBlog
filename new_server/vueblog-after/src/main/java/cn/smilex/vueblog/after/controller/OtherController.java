package cn.smilex.vueblog.after.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.entity.other.AboutMe;
import cn.smilex.vueblog.common.entity.other.BackUp;
import cn.smilex.vueblog.common.handler.AuthService;
import cn.smilex.vueblog.common.service.OtherService;
import cn.smilex.vueblog.common.util.ClassUtil;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.linecorp.armeria.server.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author smilex
 * @date 2022/12/15/16:18
 */
@SuppressWarnings({"unused", "OptionalGetWithoutIsPresent"})
@Slf4j
@PathPrefix("/other")
@ProducesJson
@RequestConverter(JacksonRequestConverterFunction.class)
@CrossOrigin
@Decorator(AuthService.class)
@Component
public class OtherController {
    private OtherService otherService;

    @Autowired
    public void setOtherService(OtherService otherService) {
        this.otherService = otherService;
    }

    /**
     * 获取关于我信息
     *
     * @return 关于我信息
     */
    @Get("/me")
    @Options("/me")
    public Result<?> aboutMe() {
        return Result.success(otherService.aboutMe());
    }

    /**
     * 更新关于我
     *
     * @param aboutMe 关于我
     * @return 结果
     */
    @Post("/me/update")
    @Options("/me/update")
    public Result<?> update(@RequestObject Optional<AboutMe> aboutMe) {
        return aboutMe.filter(v -> {
                    try {
                        return !ClassUtil.objIsNull(AboutMe.class, v, "id");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .<Result<?>>map(v -> {
                    otherService.updateMe(v);
                    return Result.success();
                })
                .orElseGet(() -> Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR));
    }

    /**
     * 查询备份信息
     *
     * @return 备份信息
     */
    @Get("/backUp/info")
    @Options("/backUp/info")
    public Result<?> backUpInfo() throws JsonProcessingException {
        return Result.success(otherService.selectBackUpInfo());
    }

    /**
     * 更新备份信息
     *
     * @return 结果
     */
    @Post("/backUp/update")
    @Options("/backUp/update")
    public Result<?> backUpUpdate(@RequestObject Optional<BackUp> backUp) throws IllegalAccessException, JsonProcessingException {
        if (CommonUtil.isEmpty(backUp) || ClassUtil.objIsNull(BackUp.class, backUp.get())) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR);
        }

        otherService.updateBackUp(backUp.get());

        return Result.success();
    }

    /**
     * 备份数据库
     *
     * @return 结果
     */
    @Get("/backUp/buy")
    @Options("/backUp/buy")
    public Result<?> backUpBuy() throws JsonProcessingException {
        return otherService.backUpBuy();
    }
}
