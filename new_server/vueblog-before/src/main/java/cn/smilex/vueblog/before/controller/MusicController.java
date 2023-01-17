package cn.smilex.vueblog.before.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.entity.music.Music;
import cn.smilex.vueblog.common.service.MusicService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Options;
import com.linecorp.armeria.server.annotation.PathPrefix;
import com.linecorp.armeria.server.annotation.ProducesJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author smilex
 */
@SuppressWarnings("all")
@PathPrefix("/music")
@ProducesJson
@CrossOrigin
@Component
public class MusicController {
    private MusicService musicService;

    @Autowired
    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;
    }

    /**
     * 查询所有音乐
     *
     * @return 音乐集合
     */
    @Get("/list")
    @Options("/list")
    public Result<?> list() {
        return Result.success(
                musicService.list(
                        new LambdaQueryWrapper<Music>()
                                .select(
                                        Music::getId,
                                        Music::getName,
                                        Music::getArtist,
                                        Music::getUrl,
                                        Music::getCover,
                                        Music::getLrc
                                )
                )
        );
    }
}
