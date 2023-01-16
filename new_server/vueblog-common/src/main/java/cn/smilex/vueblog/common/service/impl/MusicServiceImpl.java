package cn.smilex.vueblog.common.service.impl;

import cn.smilex.req.HttpResponse;
import cn.smilex.vueblog.common.config.CommonConfig;
import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.common.VueBlogConfig;
import cn.smilex.vueblog.common.entity.music.MusicSearchResult;
import cn.smilex.vueblog.common.exception.VueBlogException;
import cn.smilex.vueblog.common.service.MusicService;
import cn.smilex.vueblog.common.util.CommonUtil;
import cn.smilex.vueblog.common.util.RequestUtil;
import cn.smilex.vueblog.common.util.StructuredTaskScope;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author smilex
 */
@SuppressWarnings({"unused", "unchecked"})
@Slf4j
@Service
public class MusicServiceImpl implements MusicService {

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
}
