package cn.smilex.vueblog.common.service.impl;

import cn.smilex.req.HttpResponse;
import cn.smilex.vueblog.common.config.CommonConfig;
import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.dao.MusicDao;
import cn.smilex.vueblog.common.entity.common.Limit;
import cn.smilex.vueblog.common.entity.common.VueBlogConfig;
import cn.smilex.vueblog.common.entity.music.Music;
import cn.smilex.vueblog.common.entity.music.MusicSearchResult;
import cn.smilex.vueblog.common.exception.VueBlogException;
import cn.smilex.vueblog.common.service.MusicService;
import cn.smilex.vueblog.common.utils.CommonUtils;
import cn.smilex.vueblog.common.utils.RequestUtils;
import cn.smilex.vueblog.common.utils.StructuredTaskScope;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

/**
 * @author smilex
 */
@SuppressWarnings({"unused", "unchecked"})
@Slf4j
@Service
public class MusicServiceImpl extends ServiceImpl<MusicDao, Music> implements MusicService {

    private VueBlogConfig vueBlogConfig;

    @Autowired
    public void setVueBlogConfig(VueBlogConfig vueBlogConfig) {
        this.vueBlogConfig = vueBlogConfig;
    }

    private void checkServer() {
        // Easy check music server url
        if (StringUtils.isBlank(vueBlogConfig.getMusicServer())) {
            throw new VueBlogException(ResultCode.ERROR_SYSTEM_CONFIG_ERROR);
        }
    }

    /**
     * 搜索音乐
     *
     * @param keyWord 关键字
     * @return 结果
     */
    @Override
    public Deque<MusicSearchResult> searchMusic(String keyWord) {
        checkServer();
        return CommonUtils.tryRun(
                () -> {
                    HttpResponse httpResponse = RequestUtils.get(String.format(
                            "%s/search/?keyWords=%s",
                            vueBlogConfig.getMusicServer(),
                            keyWord
                    ));

                    JsonNode root = CommonConfig.OBJECT_MAPPER.readTree(httpResponse.getBody());
                    JsonNode songs = root.get("result")
                            .get("songs");
                    Deque<MusicSearchResult> musicSearchResultDeque = new LinkedBlockingDeque<>(songs.size());

                    JsonNode songDetailJsonNodeList = selectSongDetailByIdList(
                            CommonUtils.mapJsonNode(
                                    songs,
                                    v -> v.get("id").asInt()
                            )
                    );

                    try (StructuredTaskScope scope = new StructuredTaskScope(songs.size())) {
                        CommonUtils.tryRun(
                                () -> {
                                    for (JsonNode song : songDetailJsonNodeList.get("songs")) {
                                        scope.execute(() -> musicSearchResultDeque.add(
                                                MusicSearchResult.parse(song)
                                        ));
                                    }
                                },
                                e -> log.error("", e)
                        );
                    } catch (Exception ignore) {
                    }

                    return musicSearchResultDeque;
                },
                () -> (Deque<MusicSearchResult>) CommonConfig.EMTPY_DEQUE,
                e -> log.error("", e)
        );
    }

    /**
     * 批量查询音乐信息
     *
     * @param idList id集合
     * @return JsonNode
     */
    @Override
    public JsonNode selectSongDetailByIdList(List<Integer> idList) {

        return CommonUtils.tryRun(
                () -> {
                    HttpResponse httpResponse = RequestUtils.get(
                            String.format(
                                    "%s/song/detail?id=%s",
                                    vueBlogConfig.getMusicServer(),
                                    CommonUtils.collectionToStr(
                                            idList,
                                            Object::toString,
                                            ","
                                    )
                            )
                    );

                    return CommonConfig.OBJECT_MAPPER.readTree(httpResponse.getBody());
                },
                () -> null,
                e -> log.error("", e)
        );
    }

    /**
     * 分页查询音乐列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 音乐列表
     */
    @Override
    public Limit<Music> selectMusicPage(Long currentPage, Long pageSize) {
        Limit<Music> limit = Limit.defaultLimit(pageSize, currentPage);

        try (StructuredTaskScope scope = new StructuredTaskScope(2)) {
            scope.execute(() -> limit.setDataList(
                    this.getBaseMapper()
                            .selectMusicPage(
                                    CommonUtils.calcLimit(currentPage, pageSize),
                                    pageSize
                            ))
            );

            scope.execute(() -> {
                long count = this.count();
                limit.setTotalCount(count);
                limit.setPageCount(CommonUtils.calcPageCount(count, pageSize));
            });
        }

        return limit;
    }

    /**
     * 添加音乐
     *
     * @param music 音乐
     * @return 结果
     */
    @Override
    public boolean addMusic(Music music) {
        if (
                this.count(
                        new LambdaQueryWrapper<Music>()
                                .eq(Music::getId, music.getId())
                ) == 0
        ) {
            music.setUrl(String.format(CommonConfig.MUSIC_API_SONG_URL_TEMPLATE, vueBlogConfig.getMusicServer(), music.getId()));
            music.setLrc(String.format(CommonConfig.MUSIC_API_LRC_TEMPLATE, vueBlogConfig.getMusicServer(), music.getId()));
            music.setCreated(LocalDateTime.now());

            return this.save(music);
        }
        return false;
    }

    /**
     * 根据ID删除音乐
     *
     * @param id id
     * @return 结果
     */
    @Override
    public boolean deleteMusicById(Long id) {
        return this.remove(
                new LambdaQueryWrapper<Music>()
                        .eq(Music::getId, id)
        );
    }

    /**
     * 根据歌单ID导入音乐
     *
     * @param id 歌单ID
     * @return 结果
     */
    @Override
    public boolean playListImport(Long id) {
        List<Music> musicList = CommonUtils.tryRun(
                () -> {
                    HttpResponse httpResponse = RequestUtils.get(
                            String.format(
                                    CommonConfig.MUSIC_API_PLAY_LIST_DETAIL_TEMPLATE,
                                    vueBlogConfig.getMusicServer(),
                                    id
                            )
                    );

                    return CommonConfig.OBJECT_MAPPER.readValue(
                            httpResponse.getBody(),
                            new TypeReference<List<Music>>() {
                            }
                    );
                },
                Collections::emptyList,
                e -> log.error("", e)
        );

        if (musicList.size() == 0) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();

        musicList = musicList.stream()
                .peek(music -> music.setCreated(now))
                .collect(Collectors.toList());

        for (Music music : musicList) {
            CommonUtils.tryRun(
                    () -> {
                        if (
                                this.count(
                                        new LambdaQueryWrapper<Music>()
                                                .eq(Music::getId, music.getId())
                                ) == 0
                        ) {
                            this.save(music);
                        }
                    },
                    e -> log.error(CommonConfig.EMPTY_STRING, e)
            );
        }

        return true;
    }

    /**
     * 清空歌单
     *
     * @return 结果
     */
    @Override
    public boolean deleteAll() {
        return this.getBaseMapper()
                .deleteAll() > 0;
    }
}
