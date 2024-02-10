package vip.smilex.vueblog.common.dao;

import vip.smilex.vueblog.common.entity.user.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import vip.smilex.vueblog.common.entity.user.User;

/**
 * @author smilex
 * @date 2022/11/12/11:43
 * @since 1.0
 */
@SuppressWarnings("unused")
@Mapper
public interface UserDao extends BaseMapper<User> {
}
