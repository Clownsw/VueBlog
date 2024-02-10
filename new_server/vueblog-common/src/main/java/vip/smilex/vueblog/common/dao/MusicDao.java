package vip.smilex.vueblog.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.smilex.vueblog.common.entity.music.Music;

import java.util.List;

/**
 * @author smilex
 */
@Mapper
public interface MusicDao extends BaseMapper<Music> {

    /**
     * 分页查询音乐列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     */
    List<Music> selectMusicPage(
            @Param("currentPage") long currentPage,
            @Param("pageSize") long pageSize
    );

    /**
     * 清空歌单
     *
     * @return 结果
     */
    int deleteAll();
}
