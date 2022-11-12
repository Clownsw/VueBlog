package cn.smilex.vueblog.before.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.entity.Result;
import cn.smilex.vueblog.common.service.SystemService;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.PathPrefix;
import com.linecorp.armeria.server.annotation.ProducesJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author smilex
 * @date 2022/11/12/17:52
 * @since 1.0
 */
@SuppressWarnings("unused")
@PathPrefix("/system")
@CrossOrigin
@Component
public class SystemController {

    private SystemService systemService;

    @Autowired
    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }

    /**
     * 查询系统信息
     *
     * @return 系统信息
     */
    @Get("/info")
    @ProducesJson
    public Result<?> info() {
        return Result.success(systemService.selectSystemInfo());
    }
}
