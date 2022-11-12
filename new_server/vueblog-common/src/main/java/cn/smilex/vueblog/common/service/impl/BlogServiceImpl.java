package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.BlogDao;
import cn.smilex.vueblog.common.entity.Blog;
import cn.smilex.vueblog.common.entity.SelectShowListBlog;
import cn.smilex.vueblog.common.service.BlogService;
import cn.smilex.vueblog.common.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linecorp.armeria.common.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author smilex
 * @date 2022/11/12/11:45
 * @since 1.0
 */
@SuppressWarnings("unused")
@Service
public class BlogServiceImpl extends ServiceImpl<BlogDao, Blog> implements BlogService {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 分页查询博文
     *
     * @param currentPage 当前页
     * @param request     请求对象
     * @return 博文集合
     */
    @Override
    public List<SelectShowListBlog> selectBlogPage(
            Optional<Integer> currentPage,
            HttpRequest request
    ) {
        boolean loginStatus = userService.isLogin(request);
        return getBaseMapper().selectBlogOtherPage();
    }
}
