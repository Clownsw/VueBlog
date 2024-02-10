package vip.smilex.vueblog.after.controller

import com.linecorp.armeria.server.annotation.*
import org.springframework.stereotype.Component
import vip.smilex.vueblog.common.annotation.CrossOrigin
import vip.smilex.vueblog.common.config.ResultCode
import vip.smilex.vueblog.common.entity.common.Result
import vip.smilex.vueblog.common.entity.music.Music
import vip.smilex.vueblog.common.service.MusicService
import vip.smilex.vueblog.common.util.CommonUtils
import java.util.*

/**
 * @author smilex
 * @date 2023/12/3 17:45:42
 */
@Suppress("unused")
@PathPrefix("/music")
@ProducesJson
@RequestConverter(JacksonRequestConverterFunction::class)
@CrossOrigin
@Decorator(vip.smilex.vueblog.common.handler.AuthService::class)
@Component
class MusicController(private val musicService: MusicService) {

    /**
     * 搜索音乐
     *
     * @param keyWord 关键字
     * @return 结果
     */
    @Get("/search")
    @Options("/search")
    fun searchMusic(@Param("keyWord") keyWord: Optional<String>): Result<*> {
        return if (CommonUtils.isEmpty(keyWord)) {
            Result.fromResultCode(
                ResultCode.ERROR_REQUEST_PARAM_ERROR
            )
        } else Result.success(musicService.searchMusic(keyWord.get()))
    }

    /**
     * 分页查询音乐列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 音乐列表
     */
    @Get("/list")
    @Options("/list")
    fun list(
        @Param("currentPage") currentPage: Optional<Long?>,
        @Param("pageSize") pageSize: Optional<Long?>,
    ): Result<*> {
        return Result.success(
            musicService.selectMusicPage(
                currentPage.orElse(1L),
                pageSize.orElse(30L)
            )
        )
    }

    /**
     * 添加一个音乐
     *
     * @param music 音乐
     * @return 结果
     */
    @Post("/add")
    @Options("/add")
    fun add(@RequestObject music: Music?): Result<*> {
        return if (musicService.addMusic(music)) Result.success() else Result.error()
    }

    /**
     * 根据ID删除音乐
     *
     * @param id id
     * @return 结果
     */
    @Get("/delete")
    @Options("/delete")
    fun delete(@Param("id") id: Long?): Result<*> {
        return if (musicService.deleteMusicById(id)) Result.success() else Result.error()
    }

    /**
     * 清空歌单
     *
     * @return 结果
     */
    @Get("/deleteAll")
    @Options("/deleteAll")
    fun deleteAll(): Result<*> {
        musicService.deleteAll()
        return Result.success()
    }

    /**
     * 根据歌单ID导入音乐
     *
     * @param id 歌单ID
     * @return 结果
     */
    @Get("/playListImport")
    @Options("/playListImport")
    fun playListImport(@Param("playListId") id: Optional<Long>): Result<*> {
        if (CommonUtils.isEmpty(id) || id.get() <= 0) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR)
        }
        return if (musicService.playListImport(id.get())) Result.success() else Result.error()
    }
}