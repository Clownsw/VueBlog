package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.BlogDao;
import cn.smilex.vueblog.common.entity.*;
import cn.smilex.vueblog.common.service.BlogService;
import cn.smilex.vueblog.common.service.UserService;
import cn.smilex.vueblog.common.util.CommonUtil;
import cn.smilex.vueblog.common.util.StructuredTaskScope;
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
    private VueBlogConfig vueBlogConfig;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setVueBlogConfig(VueBlogConfig vueBlogConfig) {
        this.vueBlogConfig = vueBlogConfig;
    }

    /**
     * 分页查询博文
     *
     * @param currentPage 当前页
     * @param request     请求对象
     * @return 博文集合
     */
    @Override
    public Limit<SelectShowListBlog> selectBlogPage(
            Optional<Long> currentPage,
            HttpRequest request
    ) {
        Long _currentPage = currentPage.orElse(1L);
        Limit<SelectShowListBlog> limit = Limit.defaultLimit(
                vueBlogConfig.getVueBlogBeforeWebPageSize(),
                _currentPage
        );

        try (StructuredTaskScope scope = new StructuredTaskScope()) {
            scope.execute(() -> {
                boolean loginStatus = userService.isLogin(request);
                List<SelectShowListBlog> selectShowListBlogList = getBaseMapper().selectBlogPage(
                        CommonUtil.calcLimit(_currentPage, vueBlogConfig.getVueBlogBeforeWebPageSize()),
                        vueBlogConfig.getVueBlogBeforeWebPageSize()
                );

                // 解析标签
                for (SelectShowListBlog selectShowListBlog : selectShowListBlogList) {
                    try {
                        final String tagIds;
                        final String tagNames;

                        if ((tagIds = selectShowListBlog.getTagIds()) != null && (tagNames = selectShowListBlog.getTagNames()) != null) {
                            List<Long> tagIdList = Arrays.stream(
                                    tagIds.split(CommonUtil.COMMA))
                                    .map(Long::parseLong)
                                    .collect(Collectors.toList()
                                    );

                            List<String> tagNameList = Arrays.stream(
                                    tagNames.split(CommonUtil.COMMA))
                                    .collect(Collectors.toList()
                                    );

                            final int len = tagIdList.size();
                            List<Tag> tagList = new ArrayList<>(tagIdList.size());

                            for (int i = 0; i < len; i++) {
                                tagList.add(
                                        new Tag(tagIdList.get(i), tagNameList.get(i))
                                );
                            }

                            selectShowListBlog.setTags(tagList);
                        }
                    } catch (Exception e) {
                        log.error("", e);
                        selectShowListBlog.setTags(new ArrayList<>(0));
                    }

                    selectShowListBlog.setSort(
                            new Sort(
                                    selectShowListBlog.getSortId(),
                                    selectShowListBlog.getSortOrder(),
                                    selectShowListBlog.getSortName()
                            )
                    );
                }

                limit.setDataList(selectShowListBlogList);
            });

            scope.execute(() -> {
                final long blogCount = this.count();
                limit.setTotalCount(blogCount);
                limit.setPageCount(CommonUtil.calcPageCount(blogCount, vueBlogConfig.getVueBlogBeforeWebPageSize()));
            });
        }


        return limit;
    }
}
