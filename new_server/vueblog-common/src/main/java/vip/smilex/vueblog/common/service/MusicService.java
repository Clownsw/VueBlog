package vip.smilex.vueblog.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.databind.JsonNode;
import vip.smilex.vueblog.common.entity.common.Limit;
import vip.smilex.vueblog.common.entity.music.Music;
import vip.smilex.vueblog.common.entity.music.MusicSearchResult;

import java.util.Deque;
import java.util.List;

/**
 * @author smilex
 */
public interface MusicService extends IService<Music> {

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

    /**
     * 分页查询音乐列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 音乐列表
     */
    Limit<Music> selectMusicPage(Long currentPage, Long pageSize);

    /**
     * 添加音乐
     *
     * @param music 音乐
     * @return 结果
     */
    boolean addMusic(Music music);

    /**
     * 根据ID删除音乐
     *
     * @param id id
     * @return 结果
     */
    boolean deleteMusicById(Long id);

    /**
     * 根据歌单ID导入音乐
     *
     * @param id 歌单ID
     * @return 结果
     */
    boolean playListImport(Long id);

    /**
     * 清空歌单
     *
     * @return 结果
     */
    boolean deleteAll();
}
