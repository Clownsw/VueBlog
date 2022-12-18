package cn.smilex.vueblog.after.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.entity.sort.Sort;
import cn.smilex.vueblog.common.handler.AuthService;
import cn.smilex.vueblog.common.service.SortService;
import cn.smilex.vueblog.common.util.ClassUtil;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.linecorp.armeria.server.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author smilex
 * @date 2022/12/6/6:07
 * @since 1.0
 */
@SuppressWarnings({"unused", "OptionalGetWithoutIsPresent"})
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

    /**
     * 添加一个分类
     *
     * @return 结果
     */
    @Post("/add")
    @Options("/add")
    public Result<?> add(@RequestObject Optional<Sort> sort) throws IllegalAccessException {
        if (CommonUtil.isEmpty(sort) || ClassUtil.objIsNull(Sort.class, sort.get(), "id")) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR);
        }

        sortService.save(sort.get());
        return Result.success();
    }

    /**
     * 根据分类ID更新分类信息
     *
     * @param sort 分类信息
     * @return 结果
     */
    @Post("/update")
    @Options("/update")
    public Result<?> update(@RequestObject Optional<Sort> sort) throws IllegalAccessException {
        if (CommonUtil.isEmpty(sort) || ClassUtil.objIsNull(Sort.class, sort.get())) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR);
        }

        sortService.updateById(sort.get());

        return Result.success();
    }

    /**
     * 根据分类ID删除分类
     *
     * @param sortId 分类ID
     * @return 结果
     */
    @Get("/delete/:sortId")
    @Options("/delete/:sortId")
    public Result<?> delete(@Param("sortId") Integer sortId) {
        if (sortId > 0) {
            sortService.removeById(sortId);
        }

        return Result.success();
    }
}
