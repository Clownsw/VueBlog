package cn.smilex.vueblog.common.dao;

import cn.smilex.vueblog.common.entity.Blog;
import cn.smilex.vueblog.common.entity.SelectShowListBlog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author smilex
 * @date 2022/11/12/11:41
 * @since 1.0
 */
@SuppressWarnings("unused")
@Mapper
public interface BlogDao extends BaseMapper<Blog> {

    /**
     * 分页查询博文
     *
     * @param currentPage 当前页
     * @return 博文集合
     */
    List<SelectShowListBlog> selectBlogPage(
            @Param("pageSize") Long pageSize,
            @Param("currentPage") Long currentPage
    );
}
