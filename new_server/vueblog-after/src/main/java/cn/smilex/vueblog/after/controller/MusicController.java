package cn.smilex.vueblog.after.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.entity.music.Music;
import cn.smilex.vueblog.common.handler.AuthService;
import cn.smilex.vueblog.common.service.MusicService;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.linecorp.armeria.server.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author smilex
 */
@SuppressWarnings({"unused", "OptionalGetWithoutIsPresent"})
@Slf4j
@PathPrefix("/music")
@ProducesJson
@RequestConverter(JacksonRequestConverterFunction.class)
@CrossOrigin
@Decorator(AuthService.class)
@Component
public class MusicController {

    private MusicService musicService;

    @Autowired
    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;
    }

    /**
     * 搜索音乐
     *
     * @param keyWord 关键字
     * @return 结果
     */
    @Get("/search")
    @Options("/search")
    public Result<?> searchMusic(@Param("keyWord") Optional<String> keyWord) {
        if (CommonUtil.isEmpty(keyWord)) {
            return Result.fromResultCode(
                    ResultCode.ERROR_REQUEST_PARAM_ERROR
            );
        }

        return Result.success(musicService.searchMusic(keyWord.get()));
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
    public Result<?> list(
            @Param("currentPage") Optional<Long> currentPage,
            @Param("pageSize") Optional<Long> pageSize
    ) {
        return Result.success(
                musicService.selectMusicPage(
                        currentPage.orElse(1L),
                        pageSize.orElse(30L)
                )
        );
    }

    /**
     * 添加一个音乐
     *
     * @param music 音乐
     * @return 结果
     */
    @Post("/add")
    @Options("/add")
    public Result<?> add(@RequestObject Music music) {
        return musicService.addMusic(music) ? Result.success() : Result.error();
    }

    /**
     * 根据ID删除音乐
     *
     * @param id id
     * @return 结果
     */
    @Get("/delete")
    @Options("/delete")
    public Result<?> delete(@Param("id") Long id) {
        return musicService.deleteMusicById(id) ? Result.success() : Result.error();
    }

    /**
     * 根据歌单ID导入音乐
     *
     * @param id 歌单ID
     * @return 结果
     */
    @Get("/playListImport")
    @Options("/playListImport")
    public Result<?> playListImport(@Param("playListId") Optional<Long> id) {
        if (CommonUtil.isEmpty(id) || id.get() <= 0) {
            return Result.fromResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR);
        }

        return musicService.playListImport(id.get()) ? Result.success() : Result.error();
    }
}
