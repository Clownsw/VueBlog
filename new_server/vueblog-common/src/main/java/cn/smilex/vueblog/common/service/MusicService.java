package cn.smilex.vueblog.common.service;

import cn.smilex.vueblog.common.entity.music.MusicSearchResult;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Deque;
import java.util.List;

/**
 * @author smilex
 */
public interface MusicService {

    /**
     * 搜索音乐
     *
     * @param keyWord 关键字
     * @return 结果
     */
    Deque<MusicSearchResult> searchMusic(String keyWord);

    /**
     * 批量查询音乐信息
     *
     * @param idList id集合
     * @return JsonNode
     */
    JsonNode selectSongDetailByIdList(List<Integer> idList);
}
