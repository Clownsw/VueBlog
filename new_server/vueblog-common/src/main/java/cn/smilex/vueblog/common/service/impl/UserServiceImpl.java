package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.UserDao;
import cn.smilex.vueblog.common.entity.common.Tuple;
import cn.smilex.vueblog.common.entity.user.User;
import cn.smilex.vueblog.common.service.UserService;
import cn.smilex.vueblog.common.util.CommonUtil;
import cn.smilex.vueblog.common.util.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linecorp.armeria.common.HttpHeaderNames;
import com.linecorp.armeria.common.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author smilex
 * @date 2022/11/12/11:51
 * @since 1.0
 */
@SuppressWarnings("unused")
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    /**
     * 判断当前请求是否包含token以及token有效性来判定是否登录
     *
     * @param request 请求对象
     * @return 是否登录
     */
    @Override
    public boolean isLogin(HttpRequest request) {
        String token = request.headers()
                .get(HttpHeaderNames.AUTHORIZATION.toString().toLowerCase());

        if (StringUtils.isBlank(token)) {
            return false;
        }

        Tuple<Boolean, Map<String, Object>> result = JwtUtil.signJWTToken(token);
        return result.getLeft();
    }

    /**
     * 获取当前token中的用户信息
     *
     * @param request 请求对象
     * @return 用户信息
     */
    @Override
    public User getUserInfoByToken(HttpRequest request) {
        return getOne(
                new LambdaQueryWrapper<User>()
                        .eq(
                                User::getId, CommonUtil.checkTokenAndGetData(request).get("id")
                        )
        );
    }
}
