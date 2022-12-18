package cn.smilex.vueblog.after.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.entity.other.SystemUpdateRequest;
import cn.smilex.vueblog.common.handler.AuthService;
import cn.smilex.vueblog.common.service.SystemService;
import cn.smilex.vueblog.common.util.ClassUtil;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.linecorp.armeria.server.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author smilex
 * @date 2022/12/18/14:45
 */
@SuppressWarnings({"unused", "OptionalGetWithoutIsPresent"})
@Slf4j
@PathPrefix("/system")
@Decorator(AuthService.class)
@RequestConverter(JacksonRequestConverterFunction.class)
@ProducesJson
@CrossOrigin
@Component
public class SystemController {
    private SystemService systemService;

    @Autowired
    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }

    /**
     * 获取系统信息
     *
     * @return 系统信息
     */
    @Get("/info")
    @Options("/info")
    public Result<?> systemInfo() {
        return Result.success(
                systemService.selectSystemInfo()
        );
    }

    /**
     * 更新系统设置
     *
     * @param systemUpdateRequest 系统设置更新请求对象
     * @return 结果
     */
    @Post("/update")
    @Options("/update")
    public Result<?> update(@RequestObject Optional<SystemUpdateRequest> systemUpdateRequest) throws IllegalAccessException {
        if (CommonUtil.isEmpty(systemUpdateRequest) || ClassUtil.objIsNull(SystemUpdateRequest.class, systemUpdateRequest.get())) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR);
        }
        systemService.updateSystem(systemUpdateRequest.get());
        return Result.success();
    }
}
