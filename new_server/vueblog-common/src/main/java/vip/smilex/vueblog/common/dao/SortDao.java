package vip.smilex.vueblog.common.dao;

import vip.smilex.vueblog.common.entity.sort.Sort;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import vip.smilex.vueblog.common.entity.sort.Sort;

/**
 * @author smilex
 * @date 2022/11/12/11:42
 * @since 1.0
 */
@SuppressWarnings("unused")
@Mapper
public interface SortDao extends BaseMapper<Sort> {
}
