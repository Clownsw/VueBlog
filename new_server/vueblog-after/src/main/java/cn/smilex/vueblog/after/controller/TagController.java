package cn.smilex.vueblog.after.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.entity.tag.Tag;
import cn.smilex.vueblog.common.handler.AuthService;
import cn.smilex.vueblog.common.service.TagService;
import cn.smilex.vueblog.common.util.ClassUtil;
import cn.smilex.vueblog.common.util.CommonUtil;
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
@SuppressWarnings({"unused", "OptionalGetWithoutIsPresent"})
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
    public Result<?> exists(@Param("tagName") String tagName) {
        return Optional.ofNullable(tagName)
                .map(s -> Result.success(tagService.tagExistsByName(s)))
                .orElseGet(() -> Result.success(false));

    }

    /**
     * 添加一个标签
     *
     * @param tagName 标签名称
     * @return success
     */
    @Get("/add/:tagName")
    @Options("/add/:tagName")
    public Result<?> add(@Param("tagName") String tagName) {
        if (StringUtils.isNotBlank(tagName)) {
            tagService.addTag(tagName);
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
    public Result<?> info(@Param("tagName") String tagName) {
        if (StringUtils.isBlank(tagName)) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR);
        }
        return Result.success(tagService.selectTagByTagName(tagName));
    }

    /**
     * 查询所有标签
     *
     * @return 标签集合
     */
    @Get("/list")
    @Options("/list")
    public Result<?> list() {
        return Result.success(
                tagService.list()
        );
    }

    /**
     * 更新标签
     *
     * @return 结果
     */
    @Post("/update")
    @Options("/update")
    public Result<?> update(@RequestObject Optional<Tag> tag) throws IllegalAccessException {
        if (CommonUtil.isEmpty(tag) || ClassUtil.objIsNull(Tag.class, tag.get())) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR);
        }

        tagService.updateById(tag.get());
        return Result.success();
    }

    /**
     * 根据标签ID删除标签
     *
     * @param tagId 标签ID
     * @return 结果
     */
    @Get("/delete/:tagId")
    @Options("/delete/:tagId")
    public Result<?> delete(@Param("tagId") Long tagId) {
        tagService.removeById(tagId);
        return Result.success();
    }
}
