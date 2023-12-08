package cn.smilex.vueblog.before.controller

import cn.smilex.vueblog.common.annotation.CrossOrigin
import cn.smilex.vueblog.common.entity.common.Result
import cn.smilex.vueblog.common.entity.music.Music
import cn.smilex.vueblog.common.service.MusicService
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Options
import com.linecorp.armeria.server.annotation.PathPrefix
import com.linecorp.armeria.server.annotation.ProducesJson
import org.springframework.stereotype.Component

/**
 * @author smilex
 * @date 2023/12/3 17:19:15
 */
@Suppress("unused")
@PathPrefix("/music")
@ProducesJson
@CrossOrigin
@Component
class MusicController(private val musicService: MusicService) {

    /**
     * 查询所有音乐
     *
     * @return 音乐集合
     */
    @Get("/list")
    @Options("/list")
    fun list(): Result<*> {
        return Result.success(
            musicService.list(
                LambdaQueryWrapper<Music>()
                    .select(
                        Music::id,
                        Music::name,
                        Music::artist,
                        Music::url,
                        Music::cover,
                        Music::lrc
                    )
            )
        )
    }
}