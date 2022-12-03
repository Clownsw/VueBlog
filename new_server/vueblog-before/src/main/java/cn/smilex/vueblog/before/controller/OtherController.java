package cn.smilex.vueblog.before.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.service.OtherService;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.PathPrefix;
import com.linecorp.armeria.server.annotation.ProducesJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author smilex
 * @date 2022/11/12/17:22
 * @since 1.0
 */
@SuppressWarnings("unused")
@PathPrefix("/other")
@CrossOrigin
@Component
public class OtherController {

    private OtherService otherService;

    @Autowired
    public void setOtherService(OtherService otherService) {
        this.otherService = otherService;
    }

    /**
     * 查询页面底部脚本
     *
     * @return 页面底部脚本
     */
    @Get("/footer")
    @ProducesJson
    public Result<?> footer() {
        return Result.success(otherService.selectPageFooter());
    }

    /**
     * 获取关于我信息
     *
     * @return 关于我信息
     */
    @Get("/me")
    @ProducesJson
    public Result<?> aboutMe() {
        return Result.success(otherService.aboutMe());
    }
}
