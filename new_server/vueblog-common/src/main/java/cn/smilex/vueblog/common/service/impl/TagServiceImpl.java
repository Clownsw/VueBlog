package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.TagDao;
import cn.smilex.vueblog.common.entity.tag.SelectBlogTag;
import cn.smilex.vueblog.common.entity.tag.Tag;
import cn.smilex.vueblog.common.service.TagService;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author smilex
 * @date 2022/11/12/11:50
 * @since 1.0
 */
@SuppressWarnings("unused")
@Service
public class TagServiceImpl extends ServiceImpl<TagDao, Tag> implements TagService {

    /**
     * 根据博文ID查询所有标签
     *
     * @param blogId 博文ID
     * @return 所有标签
     */
    @Override
    public List<Tag> selectTagListByBlogId(Long blogId) {
        return this.getBaseMapper()
                .selectTagByBlogId(blogId);
    }

    /**
     * 根据标签名称查询标签信息
     *
     * @param tagName 标签名称
     * @return 标签信息
     */
    @Override
    public Tag selectTagByTagName(String tagName) {
        return getOne(
                new LambdaQueryWrapper<Tag>()
                        .eq(Tag::getName, tagName)
        );
    }

    /**
     * 批量删除博文标签
     *
     * @param blogId    博文ID
     * @param tagIdList 标签ID集合
     * @return 影响行数
     */
    @Override
    public int batchRemoveBlogTag(Long blogId, List<Long> tagIdList) {
        return this.getBaseMapper()
                .batchRemoveBlogTag(
                        blogId,
                        CommonUtil.collectionToStr(
                                tagIdList,
                                Object::toString,
                                ","
                        )
                );
    }

    /**
     * 批量添加博文标签
     *
     * @param blogId      博文ID
     * @param blogTagList 博文标签集合
     * @return 影响行数
     */
    @Override
    public int batchAddBlogTag(Long blogId, List<SelectBlogTag> blogTagList) {
        return this.getBaseMapper()
                .batchAddBlogTag(
                        CommonUtil.collectionToStr(
                                blogTagList,
                                v -> String.format("(%d, %d, %d)", blogId, v.getId(), v.getSort()),
                                ","
                        )
                );
    }

    /**
     * 批量更新博文标签
     *
     * @param blogId      博文ID
     * @param blogTagList 博文标签集合
     * @return 影响行数
     */
    @Override
    public int batchUpdateBlogTag(Long blogId, List<SelectBlogTag> blogTagList) {
        return this.getBaseMapper()
                .batchUpdateBlogTag(
                        CommonUtil.collectionToStr(blogTagList, v -> String.format("WHEN %d THEN %d", v.getId(), v.getSort()), " ") + " END",
                        blogId,
                        CommonUtil.collectionToStr(
                                blogTagList,
                                v -> v.getId().toString(),
                                ","
                        )
                );
    }

    /**
     * 判断指定标签是否存在
     *
     * @param tagName 标签名称
     * @return 是否存在
     */
    @Override
    public boolean tagExistsByName(String tagName) {
        return count(
                new LambdaQueryWrapper<Tag>()
                        .eq(Tag::getName, tagName)
        ) != 0;
    }

    /**
     * 添加一个标签
     *
     * @param tagName 标签名称
     * @return 是否成功
     */
    @Override
    public boolean addTag(String tagName) {
        if (selectTagByTagName(tagName) == null) {
            return this.save(new Tag(null, tagName));
        }
        return false;
    }
}
