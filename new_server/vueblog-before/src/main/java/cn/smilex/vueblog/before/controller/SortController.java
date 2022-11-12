package cn.smilex.vueblog.before.controller;

import cn.smilex.vueblog.common.entity.Result;
import cn.smilex.vueblog.common.service.SortService;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.PathPrefix;
import com.linecorp.armeria.server.annotation.ProducesJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author smilex
 * @date 2022/11/12/11:55
 * @since 1.0
 */
@SuppressWarnings("unused")
@PathPrefix("/sort")
@Component
public class SortController {
    private SortService sortService;

    @Autowired
    public void setSortService(SortService sortService) {
        this.sortService = sortService;
    }

    @Get("/selectAllSort")
    @ProducesJson
    public Result<?> selectAllSort() {
        return Result.success(sortService.list());
    }
}
