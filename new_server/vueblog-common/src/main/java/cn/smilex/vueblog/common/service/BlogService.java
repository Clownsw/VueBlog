package cn.smilex.vueblog.common.service;

import cn.smilex.vueblog.common.entity.Blog;
import cn.smilex.vueblog.common.entity.SelectShowListBlog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linecorp.armeria.common.HttpRequest;

import java.util.List;
import java.util.Optional;

/**
 * @author smilex
 * @date 2022/11/12/11:44
 * @since 1.0
 */
public interface BlogService extends IService<Blog> {

    /**
     * 分页查询博文
     *
     * @param currentPage 当前页
     * @param request     请求对象
     * @return 博文集合
     */
    List<SelectShowListBlog> selectBlogPage(
            Optional<Integer> currentPage,
            HttpRequest request
    );
}
