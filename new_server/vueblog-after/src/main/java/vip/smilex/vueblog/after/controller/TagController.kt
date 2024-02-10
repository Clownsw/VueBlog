package vip.smilex.vueblog.after.controller

import com.linecorp.armeria.server.annotation.*
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import vip.smilex.vueblog.common.annotation.CrossOrigin
import vip.smilex.vueblog.common.config.ResultCode
import vip.smilex.vueblog.common.entity.common.Result
import vip.smilex.vueblog.common.entity.tag.Tag
import vip.smilex.vueblog.common.service.TagService
import vip.smilex.vueblog.common.util.ClassUtils
import vip.smilex.vueblog.common.util.CommonUtils
import java.util.*

/**
 * @author smilex
 * @date 2023/12/3 17:37:52
 */
@Suppress("unused")
@PathPrefix("/tag")
@ProducesJson
@RequestConverter(JacksonRequestConverterFunction::class)
@Decorator(vip.smilex.vueblog.common.handler.AuthService::class)
@CrossOrigin
@Component
class TagController(private val tagService: TagService) {

    /**
     * 判断指定标签是否存在
     *
     * @param tagName 标签名称
     * @return 是否存在
     */
    @Get("/exists/:tagName")
    @Options("/exists/:tagName")
    fun exists(@Param("tagName") tagName: String?): Result<*>? {
        return Optional.ofNullable(tagName)
            .map { s: String? ->
                Result.success(
                    tagService.tagExistsByName(s)
                )
            }
            .orElseGet {
                Result.success(
                    false
                )
            }
    }

    /**
     * 添加一个标签
     *
     * @param tagName 标签名称
     * @return success
     */
    @Get("/add/:tagName")
    @Options("/add/:tagName")
    fun add(@Param("tagName") tagName: String?): Result<*>? {
        if (StringUtils.isNotBlank(tagName)) {
            tagService.addTag(tagName)
        }
        return Result.success()
    }

    /**
     * 根据标签名称查询标签信息
     *
     * @param tagName 标签名称
     * @return 标签信息
     */
    @Get("/:tagName")
    @Options("/:tagName")
    fun info(@Param("tagName") tagName: String?): Result<*>? {
        return if (StringUtils.isBlank(tagName)) {
            Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR)
        } else Result.success(tagService.selectTagByTagName(tagName))
    }

    /**
     * 查询所有标签
     *
     * @return 标签集合
     */
    @Get("/list")
    @Options("/list")
    fun list(): Result<*>? {
        return Result.success(
            tagService.list()
        )
    }

    /**
     * 更新标签
     *
     * @return 结果
     */
    @Post("/update")
    @Options("/update")
    fun update(@RequestObject tag: Optional<Tag>): Result<*>? {
        if (CommonUtils.isEmpty(tag) || ClassUtils.objIsNull(
                Tag::class.java, tag.get()
            )
        ) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR)
        }
        tagService.updateById(tag.get())
        return Result.success()
    }

    /**
     * 根据标签ID删除标签
     *
     * @param tagId 标签ID
     * @return 结果
     */
    @Get("/delete/:tagId")
    @Options("/delete/:tagId")
    fun delete(@Param("tagId") tagId: Long?): Result<*>? {
        tagService.removeById(tagId)
        return Result.success()
    }
}