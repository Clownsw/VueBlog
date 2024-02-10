package vip.smilex.vueblog.common.service.impl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.core.toolkit.support.SFunction
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import lombok.extern.slf4j.Slf4j
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import vip.smilex.vueblog.common.config.CommonConfig
import vip.smilex.vueblog.common.dao.MusicDao
import vip.smilex.vueblog.common.entity.common.Limit
import vip.smilex.vueblog.common.entity.common.VueBlogConfig
import vip.smilex.vueblog.common.entity.music.Music
import vip.smilex.vueblog.common.entity.music.MusicSearchResult
import vip.smilex.vueblog.common.util.CollectionUtils
import vip.smilex.vueblog.common.util.CommonUtils.Companion.calcLimit
import vip.smilex.vueblog.common.util.CommonUtils.Companion.calcPageCount
import vip.smilex.vueblog.common.util.CommonUtils.Companion.collectionToStr
import vip.smilex.vueblog.common.util.CommonUtils.Companion.mapJsonNode
import vip.smilex.vueblog.common.util.CommonUtils.Companion.tryRun
import vip.smilex.vueblog.common.util.CommonUtils.Companion.tryRunNoReturn
import vip.smilex.vueblog.common.util.RequestUtils
import vip.smilex.vueblog.common.util.StructuredTaskScope
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.LinkedBlockingDeque
import java.util.stream.Collectors

/**
 * @author smilex
 * @date 2023/12/8 13:06:40
 */
@Suppress("unused")
@Slf4j
@Service
open class MusicServiceImpl(private val vueBlogConfig: VueBlogConfig) : ServiceImpl<MusicDao, Music>(),
    vip.smilex.vueblog.common.service.MusicService {
    companion object {
        @JvmStatic
        val LOG: Logger = LoggerFactory.getLogger(MusicServiceImpl::javaClass.javaClass)
    }

    private fun checkServer() {
        // Easy check music server url
        if (StringUtils.isBlank(vueBlogConfig.musicServer)) {
            throw vip.smilex.vueblog.common.exception.VueBlogException(vip.smilex.vueblog.common.config.ResultCode.ERROR_SYSTEM_CONFIG_ERROR)
        }
    }

    /**
     * 搜索音乐
     *
     * @param keyWord 关键字
     * @return 结果
     */
    override fun searchMusic(keyWord: String?): Deque<MusicSearchResult> {
        checkServer()
        return tryRun(
            {
                val httpResponse = RequestUtils.get(
                    String.format(
                        "%s/search/?keyWords=%s",
                        vueBlogConfig.musicServer,
                        keyWord
                    )
                )
                val root = CommonConfig.OBJECT_MAPPER.readTree(httpResponse.body)
                val songs = root["result"]["songs"]
                val musicSearchResultDeque: Deque<MusicSearchResult> =
                    LinkedBlockingDeque(songs.size())
                val songDetailJsonNodeList = selectSongDetailByIdList(
                    mapJsonNode(
                        songs
                    ) { jsonNode: JsonNode -> jsonNode["id"].asInt() }
                )

                StructuredTaskScope(songs.size()).use { scope ->
                    tryRunNoReturn(
                        {
                            for (song in songDetailJsonNodeList!!["songs"]) {
                                scope.execute {
                                    musicSearchResultDeque.add(
                                        MusicSearchResult.parse(song)
                                    )
                                }
                            }
                        }
                    ) { e: Throwable? ->
                        LOG.error(
                            "",
                            e
                        )
                    }
                }
                musicSearchResultDeque
            },
            { CommonConfig.emptyDeque() }
        ) { e: Throwable? ->
            LOG.error(
                "",
                e
            )
        }
    }

    /**
     * 批量查询音乐信息
     *
     * @param idList id集合
     * @return JsonNode
     */
    override fun selectSongDetailByIdList(idList: List<Int>): JsonNode? {
        return tryRun(
            {
                val httpResponse = RequestUtils.get(
                    String.format(
                        "%s/song/detail?id=%s",
                        vueBlogConfig.musicServer,
                        collectionToStr(
                            idList,
                            { obj: Any -> obj.toString() },
                            ","
                        )
                    )
                )
                CommonConfig.OBJECT_MAPPER.readTree(httpResponse.body)
            },
            { null }
        ) { e: Throwable ->
            LOG.error(
                "",
                e
            )
        }
    }

    /**
     * 分页查询音乐列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 音乐列表
     */
    override fun selectMusicPage(currentPage: Long, pageSize: Long): Limit<Music> {
        val limit = Limit.defaultLimit<Music>(pageSize, currentPage)
        StructuredTaskScope(2).use { scope ->
            scope.execute {
                limit.dataList = getBaseMapper()
                    .selectMusicPage(
                        calcLimit(currentPage, pageSize),
                        pageSize
                    )
            }
            scope.execute {
                val count = this.count()
                limit.totalCount = count
                limit.pageCount = calcPageCount(count, pageSize)
            }
        }
        return limit
    }

    /**
     * 添加音乐
     *
     * @param music 音乐
     * @return 结果
     */
    override fun addMusic(music: Music): Boolean {
        if (this.count(
                LambdaQueryWrapper<Music>()
                    .eq(SFunction<Music, Any> { obj: Music -> obj.id }, music.id)
            ) == 0L
        ) {
            music.url =
                String.format(CommonConfig.MUSIC_API_SONG_URL_TEMPLATE, vueBlogConfig.musicServer, music.id)
            music.lrc = String.format(CommonConfig.MUSIC_API_LRC_TEMPLATE, vueBlogConfig.musicServer, music.id)
            music.created = LocalDateTime.now()
            return save(music)
        }
        return false
    }

    /**
     * 根据ID删除音乐
     *
     * @param id id
     * @return 结果
     */
    override fun deleteMusicById(id: Long?): Boolean {
        return this.remove(
            LambdaQueryWrapper<Music>()
                .eq(SFunction<Music, Any> { obj: Music -> obj.id }, id)
        )
    }

    /**
     * 根据歌单ID导入音乐
     *
     * @param id 歌单ID
     * @return 结果
     */
    override fun playListImport(id: Long?): Boolean {
        var musicList = tryRun<List<Music>>(
            {
                val httpResponse = RequestUtils.get(
                    String.format(
                        CommonConfig.MUSIC_API_PLAY_LIST_DETAIL_TEMPLATE,
                        vueBlogConfig.musicServer,
                        id
                    )
                )

                CommonConfig.OBJECT_MAPPER.readValue(
                    httpResponse.body,
                    object : TypeReference<List<Music>>() {}
                )
            },
            {
                Collections.emptyList()
            },
            { e: Throwable ->
                LOG.error(
                    "",
                    e
                )
            }
        )

        if (CollectionUtils.isEmpty(musicList)) {
            return false
        }

        val now = LocalDateTime.now()
        musicList = musicList.stream()
            .peek { music: Music -> music.created = now }
            .collect(Collectors.toList())

        for (music in musicList) {
            tryRunNoReturn(
                {
                    if (this.count(
                            LambdaQueryWrapper<Music>()
                                .eq(SFunction<Music, Any> { obj: Music -> obj.id }, music.id)
                        ) == 0L
                    ) {
                        save(music)
                    }
                }
            ) { e: Throwable? ->
                LOG.error(
                    CommonConfig.EMPTY_STRING,
                    e
                )
            }
        }
        return true
    }

    /**
     * 清空歌单
     *
     * @return 结果
     */
    override fun deleteAll(): Boolean {
        return getBaseMapper()
            .deleteAll() > 0
    }
}
