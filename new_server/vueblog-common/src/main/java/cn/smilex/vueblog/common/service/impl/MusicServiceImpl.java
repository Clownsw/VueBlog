package cn.smilex.vueblog.common.service.impl;

import cn.smilex.req.HttpResponse;
import cn.smilex.vueblog.common.config.CommonConfig;
import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.dao.MusicDao;
import cn.smilex.vueblog.common.entity.common.VueBlogConfig;
import cn.smilex.vueblog.common.entity.music.Music;
import cn.smilex.vueblog.common.entity.music.MusicSearchResult;
import cn.smilex.vueblog.common.exception.VueBlogException;
import cn.smilex.vueblog.common.service.MusicService;
import cn.smilex.vueblog.common.util.CommonUtil;
import cn.smilex.vueblog.common.util.RequestUtil;
import cn.smilex.vueblog.common.util.StructuredTaskScope;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

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
        return CommonUtil.tryRun(
                () -> {
                    HttpResponse httpResponse = RequestUtil.get(String.format(
                            "%s/search/?keyWords=%s",
                            vueBlogConfig.getMusicServer(),
                            keyWord
                    ));

                    JsonNode root = CommonConfig.OBJECT_MAPPER.readTree(httpResponse.getBody());
                    JsonNode songs = root.get("result")
                            .get("songs");
                    Deque<MusicSearchResult> musicSearchResultDeque = new LinkedBlockingDeque<>(songs.size());

                    JsonNode songDetailJsonNodeList = selectSongDetailByIdList(
                            CommonUtil.mapJsonNode(
                                    songs,
                                    v -> v.get("id").asInt()
                            )
                    );

                    try (StructuredTaskScope scope = new StructuredTaskScope(songs.size())) {
                        CommonUtil.tryRun(
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

        return CommonUtil.tryRun(
                () -> {
                    HttpResponse httpResponse = RequestUtil.get(
                            String.format(
                                    "%s/song/detail?id=%s",
                                    vueBlogConfig.getMusicServer(),
                                    CommonUtil.collectionToStr(
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
    public Page<Music> selectMusicPage(Long currentPage, Long pageSize) {
        Page<Music> page = new Page<>(currentPage, pageSize);
        return page(page);
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
            music.setUrl(String.format("%s/vueblog/song/url?id=%d", vueBlogConfig.getMusicServer(), music.getId()));
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
}
