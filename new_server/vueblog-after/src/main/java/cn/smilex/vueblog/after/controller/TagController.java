package cn.smilex.vueblog.after.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.handler.AuthService;
import cn.smilex.vueblog.common.service.TagService;
import com.linecorp.armeria.server.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author smilex
 * @date 2022/12/10/15:15
 */
@SuppressWarnings("unused")
@Slf4j
@PathPrefix("/tag")
@ProducesJson
@RequestConverter(JacksonRequestConverterFunction.class)
@Decorator(AuthService.class)
@CrossOrigin
@Component
public class TagController {
    private TagService tagService;

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * 判断指定标签是否存在
     *
     * @param tagName 标签名称
     * @return 是否存在
     */
    @Get("/exists/:tagName")
    @Options("/exists/:tagName")
    public Result<?> exists(@Param("tagName") Optional<String> tagName) {
        return tagName.map(s -> Result.success(tagService.tagExistsByName(s))).orElseGet(() -> Result.success(false));

    }

    /**
     * 添加一个标签
     *
     * @param tagName 标签名称
     * @return success
     */
    @Get("/add/:tagName")
    @Options("/add/:tagName")
    public Result<?> add(@Param("tagName") Optional<String> tagName) {
        if (tagName.isPresent() && StringUtils.isNotBlank(tagName.get())) {
            tagService.addTag(tagName.get());
        }

        return Result.success();
    }

    /**
     * 根据标签名称查询标签信息
     *
     * @param tagName 标签名称
     * @return 标签信息
     */
    @Get("/:tagName")
    @Options("/:tagName")
    public Result<?> info(@Param("tagName") Optional<String> tagName) {
        if (!tagName.isPresent() || StringUtils.isBlank(tagName.get())) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR);
        }
        return Result.success(tagService.selectTagByTagName(tagName.get()));
    }
}
