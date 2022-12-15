package cn.smilex.vueblog.common.dao;

import cn.smilex.vueblog.common.entity.other.AboutMe;
import cn.smilex.vueblog.common.entity.other.Other;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author smilex
 * @date 2022/11/12/11:42
 * @since 1.0
 */
@SuppressWarnings("unused")
@Mapper
public interface OtherDao extends BaseMapper<Other> {

    /**
     * 更新关于我
     *
     * @param aboutMe 关于我
     * @return 是否成功
     */
    boolean updateMe(AboutMe aboutMe);
}
