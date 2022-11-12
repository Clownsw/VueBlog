package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.BlogDao;
import cn.smilex.vueblog.common.entity.Blog;
import cn.smilex.vueblog.common.entity.SelectShowListBlog;
import cn.smilex.vueblog.common.entity.Sort;
import cn.smilex.vueblog.common.entity.Tag;
import cn.smilex.vueblog.common.service.BlogService;
import cn.smilex.vueblog.common.service.UserService;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linecorp.armeria.common.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author smilex
 * @date 2022/11/12/11:45
 * @since 1.0
 */
@SuppressWarnings("unused")
@Slf4j
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
        List<SelectShowListBlog> selectShowListBlogList = getBaseMapper().selectBlogPage(currentPage.orElse(1));

        // 解析标签
        for (SelectShowListBlog selectShowListBlog : selectShowListBlogList) {
            try {
                List<Long> tagIdList = Arrays.stream(selectShowListBlog.getTagIds().split(CommonUtil.COMMA))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

                List<String> tagNameList = Arrays.stream(selectShowListBlog.getTagNames()
                        .split(CommonUtil.COMMA)).
                        collect(Collectors.toList());

                final int len = tagIdList.size();
                List<Tag> tagList = new ArrayList<>(tagIdList.size());

                for (int i = 0; i < len; i++) {
                    tagList.add(
                            new Tag(tagIdList.get(i), tagNameList.get(i))
                    );
                }

                selectShowListBlog.setTags(tagList);
            } catch (Exception e) {
                log.error("", e);
                selectShowListBlog.setTags(new ArrayList<>(0));
            }

            log.info("{}", selectShowListBlog);
            selectShowListBlog.setSort(
                    new Sort(
                            selectShowListBlog.getSortId(),
                            selectShowListBlog.getSortOrder(),
                            selectShowListBlog.getSortName()
                    )
            );
        }

        return selectShowListBlogList;
    }
}
