package cn.smilex.vueblog.after.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.handler.AuthService;
import cn.smilex.vueblog.common.service.SortService;
import com.linecorp.armeria.server.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author smilex
 * @date 2022/12/6/6:07
 * @since 1.0
 */
@SuppressWarnings("unused")
@Slf4j
@PathPrefix("/sort")
@ProducesJson
@Decorator(AuthService.class)
@RequestConverter(JacksonRequestConverterFunction.class)
@CrossOrigin
@Component
public class SortController {
    private SortService sortService;

    @Autowired
    public void setSortService(SortService sortService) {
        this.sortService = sortService;
    }

    /**
     * 查询所有分类
     *
     * @return 所有分类
     */
    @Get("/list")
    @Options("/list")
    public Result<?> list() {
        return Result.success(sortService.selectSortList());
    }
}
