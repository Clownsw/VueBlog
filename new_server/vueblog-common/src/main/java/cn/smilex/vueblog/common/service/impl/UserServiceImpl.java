package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.UserDao;
import cn.smilex.vueblog.common.entity.User;
import cn.smilex.vueblog.common.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author smilex
 * @date 2022/11/12/11:51
 * @since 1.0
 */
@SuppressWarnings("unused")
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
}
