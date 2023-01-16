package cn.smilex.vueblog.after.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.common.Result;
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
     * @param musicName 音乐名称
     * @return 结果
     */
    @Get("/search")
    @Options("/search")
    public Result<?> searchMusic(@Param("musicName") Optional<String> musicName) {
        if (CommonUtil.isEmpty(musicName)) {
            return Result.fromResultCode(
                    ResultCode.ERROR_REQUEST_PARAM_ERROR
            );
        }

        return Result.success(musicService.searchMusic(musicName.get()));
    }
}
