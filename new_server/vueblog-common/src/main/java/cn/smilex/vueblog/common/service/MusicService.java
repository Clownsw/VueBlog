package cn.smilex.vueblog.common.service;

import cn.smilex.vueblog.common.entity.music.MusicSearchResult;

import java.util.List;

/**
 * @author smilex
 */
public interface MusicService {

    /**
     * 搜索音乐
     *
     * @param musicName 音乐名称
     * @return 结果
     */
    List<MusicSearchResult> searchMusic(String musicName);
}
