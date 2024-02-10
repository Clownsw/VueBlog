package vip.smilex.vueblog.common.dao;

import vip.smilex.vueblog.common.entity.other.System;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import vip.smilex.vueblog.common.entity.other.System;

/**
 * @author smilex
 * @date 2022/11/12/11:44
 * @since 1.0
 */
@SuppressWarnings("unused")
@Mapper
public interface SystemDao extends BaseMapper<System> {
}
