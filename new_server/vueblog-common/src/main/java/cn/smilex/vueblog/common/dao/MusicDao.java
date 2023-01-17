package cn.smilex.vueblog.common.dao;

import cn.smilex.vueblog.common.entity.common.Limit;
import cn.smilex.vueblog.common.entity.music.Music;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author smilex
 */
@Mapper
public interface MusicDao extends BaseMapper<Music> {

    /**
     * 分页查询音乐列表
     *
     * @param limit 分页信息
     * @return 音乐列表
     */
    List<Music> selectMusicPage(Limit<Music> limit);
}
