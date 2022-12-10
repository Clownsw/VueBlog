package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.BlogDao;
import cn.smilex.vueblog.common.entity.blog.Blog;
import cn.smilex.vueblog.common.entity.blog.RequestBlog;
import cn.smilex.vueblog.common.entity.blog.SelectBlogInfo;
import cn.smilex.vueblog.common.entity.blog.SelectShowBlog;
import cn.smilex.vueblog.common.entity.common.Limit;
import cn.smilex.vueblog.common.entity.common.Triplet;
import cn.smilex.vueblog.common.entity.common.VueBlogConfig;
import cn.smilex.vueblog.common.entity.sort.Sort;
import cn.smilex.vueblog.common.entity.tag.SelectBlogTag;
import cn.smilex.vueblog.common.entity.tag.Tag;
import cn.smilex.vueblog.common.service.BlogService;
import cn.smilex.vueblog.common.service.TagService;
import cn.smilex.vueblog.common.util.CommonUtil;
import cn.smilex.vueblog.common.util.ListUtil;
import cn.smilex.vueblog.common.util.StructuredTaskScope;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author smilex
 * @date 2022/11/12/11:45
 * @since 1.0
 */
@SuppressWarnings({"unused", "Duplicates"})
@Slf4j
@Service
public class BlogServiceImpl extends ServiceImpl<BlogDao, Blog> implements BlogService {

    private VueBlogConfig vueBlogConfig;
    private TagService tagService;

    @Autowired
    public void setVueBlogConfig(VueBlogConfig vueBlogConfig) {
        this.vueBlogConfig = vueBlogConfig;
    }

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    private void blogParseTag(List<SelectShowBlog> selectShowBlogList) {
        // 解析标签
        for (SelectShowBlog selectShowBlog : selectShowBlogList) {
            try {
                final String tagIds;
                final String tagNames;

                if ((tagIds = selectShowBlog.getTagIds()) != null && (tagNames = selectShowBlog.getTagNames()) != null) {
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

                    selectShowBlog.setTags(tagList);
                }
            } catch (Exception e) {
                log.error("", e);
                selectShowBlog.setTags(new ArrayList<>(0));
            }

            selectShowBlog.setSort(
                    new Sort(
                            selectShowBlog.getSortId(),
                            selectShowBlog.getSortOrder(),
                            selectShowBlog.getSortName()
                    )
            );
        }
    }

    /**
     * 分页查询博文
     *
     * @param currentPage 当前页
     * @return 博文集合
     */
    @Override
    public Limit<SelectShowBlog> selectBlogPage(Long currentPage) {
        return selectBlogPage(currentPage, vueBlogConfig.getVueBlogBeforeWebPageSize());
    }

    /**
     * 分页查询博文
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 博文集合
     */
    @Override
    public Limit<SelectShowBlog> selectBlogPage(
            Long currentPage,
            Long pageSize
    ) {
        Limit<SelectShowBlog> limit = Limit.defaultLimit(
                pageSize,
                currentPage
        );

        try (StructuredTaskScope scope = new StructuredTaskScope()) {
            scope.execute(() -> {
                List<SelectShowBlog> selectShowBlogList = getBaseMapper().selectBlogPage(
                        CommonUtil.calcLimit(currentPage, pageSize),
                        pageSize
                );

                blogParseTag(selectShowBlogList);

                limit.setDataList(selectShowBlogList);
            });

            scope.execute(() -> {
                final long blogCount = this.count();
                limit.setTotalCount(blogCount);
                limit.setPageCount(CommonUtil.calcPageCount(blogCount, pageSize));
            });
        }

        return limit;
    }

    /**
     * 根据ID获取文章信息
     *
     * @param id id
     * @return 文章信息
     */
    @Override
    public SelectBlogInfo selectSelectShowBlogById(Long id) {
        SelectBlogInfo selectBlogInfo = getBaseMapper().selectBlogById(id);

        if (selectBlogInfo != null) {
            blogParseTag(ListUtil.of(selectBlogInfo));
        }

        return selectBlogInfo;
    }

    /**
     * 根据ID查询博文
     *
     * @param id ID
     * @return 博文
     */
    @Override
    public Blog selectBlogById(Long id) {
        return this.getOne(
                new LambdaQueryWrapper<Blog>()
                        .eq(Blog::getId, id)
        );
    }

    /**
     * 根据指定标签分页查询所有文章
     *
     * @param currentPage 当前页
     * @param tagId       标签ID
     * @return 指定标签下的所有文章
     */
    @Override
    public Limit<SelectShowBlog> selectBlogPageByTagId(Long currentPage, Long tagId) {
        Limit<SelectShowBlog> limit = Limit.defaultLimit(
                vueBlogConfig.getVueBlogBeforeWebPageSize(),
                currentPage
        );

        try (StructuredTaskScope scope = new StructuredTaskScope()) {
            scope.execute(() -> {
                List<SelectShowBlog> selectShowBlogList = getBaseMapper().selectBlogPageByTagId(
                        CommonUtil.calcLimit(currentPage, vueBlogConfig.getVueBlogBeforeWebPageSize()),
                        vueBlogConfig.getVueBlogBeforeWebPageSize(),
                        tagId
                );

                blogParseTag(selectShowBlogList);

                limit.setDataList(selectShowBlogList);
            });

            scope.execute(() -> {
                final long blogCount = getBaseMapper().selectBlogCountByTagId(tagId);
                limit.setTotalCount(blogCount);
                limit.setPageCount(CommonUtil.calcPageCount(blogCount, vueBlogConfig.getVueBlogBeforeWebPageSize()));
            });
        }

        return limit;
    }

    /**
     * 分页查询指定分类下的所有文章
     *
     * @param currentPage 当前页
     * @param sortId      分类ID
     * @return 指定分类下的所有文章
     */
    @Override
    public Limit<SelectShowBlog> selectBlogPageBySortId(Long currentPage, Integer sortId) {
        Limit<SelectShowBlog> limit = Limit.defaultLimit(
                vueBlogConfig.getVueBlogBeforeWebPageSize(),
                currentPage
        );

        try (StructuredTaskScope scope = new StructuredTaskScope()) {
            scope.execute(() -> {
                List<SelectShowBlog> selectShowBlogList = getBaseMapper().selectBlogPageBySortId(
                        CommonUtil.calcLimit(currentPage, vueBlogConfig.getVueBlogBeforeWebPageSize()),
                        vueBlogConfig.getVueBlogBeforeWebPageSize(),
                        sortId
                );

                blogParseTag(selectShowBlogList);

                limit.setDataList(selectShowBlogList);
            });

            scope.execute(() -> {
                final long blogCount = getBaseMapper().selectBlogCountBySortId(sortId);
                limit.setTotalCount(blogCount);
                limit.setPageCount(CommonUtil.calcPageCount(blogCount, vueBlogConfig.getVueBlogBeforeWebPageSize()));
            });
        }

        return limit;
    }

    /**
     * 根据ID和秘钥查询文章信息
     *
     * @param id  id
     * @param key 秘钥
     * @return 文章信息
     */
    @Override
    public Blog selectBlogByIdAndKey(Long id, String key) {
        return getBaseMapper().selectBlogByIdAndKey(id, key);
    }

    /**
     * 分页查询博文列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 博文列表
     */
    @Override
    public Page<Blog> blogPage(Long currentPage, Long pageSize) {
        return this.page(new Page<>(currentPage, pageSize));
    }

    /**
     * 添加或编辑博文
     *
     * @param requestBlog 请求博文对象
     * @return 结果
     */
    @Override
    public boolean edit(RequestBlog requestBlog) {
        try {
            // 添加
            if (requestBlog.getId() == null) {
                insertBlog(requestBlog);
            }
            // 编辑
            else {
                updateBlog(requestBlog);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 更新博文
     *
     * @param requestBlog 请求博文对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBlog(RequestBlog requestBlog) {
        List<Long> idList = requestBlog.getTag()
                .stream()
                .map(SelectBlogTag::getId)
                .collect(Collectors.toList());

        Blog blog = this.selectBlogById(requestBlog.getId());
        if (blog != null) {
            List<Long> oldTagIdList = tagService.selectTagByBlogId(requestBlog.getId())
                    .stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());

            Triplet<List<Long>, List<Long>, List<Long>> tagCalcResult = CommonUtil.getDelAndAddAndDefaultList(oldTagIdList, idList);

        }
    }

    /**
     * 添加博文
     *
     * @param requestBlog 请求博文对象
     */
    @Override
    public void insertBlog(RequestBlog requestBlog) {

    }
}
