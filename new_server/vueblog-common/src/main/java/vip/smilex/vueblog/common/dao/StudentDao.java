package vip.smilex.vueblog.common.dao;

import vip.smilex.vueblog.common.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import vip.smilex.vueblog.common.entity.Student;

/**
 * @author smilex
 * @date 2022/11/11/21:28
 * @since 1.0
 */
@Mapper
public interface StudentDao extends BaseMapper<Student> {
}
