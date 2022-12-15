package cn.smilex.vueblog.common.service;

import cn.smilex.vueblog.common.entity.tag.SelectBlogTag;
import cn.smilex.vueblog.common.entity.tag.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author smilex
 * @date 2022/11/12/11:45
 * @since 1.0
 */
@SuppressWarnings("all")
public interface TagService extends IService<Tag> {

    /**
     * 根据博文ID查询所有标签
     *
     * @param blogId 博文ID
     * @return 所有标签
     */
    List<Tag> selectTagListByBlogId(Long blogId);

    /**
     * 根据标签名称查询标签信息
     *
     * @param tagName 标签名称
     * @return 标签信息
     */
    Tag selectTagByTagName(String tagName);

    /**
     * 批量删除博文标签
     *
     * @param blogId    博文ID
     * @param tagIdList 标签ID集合
     * @return 影响行数
     */
    int batchRemoveBlogTag(Long blogId, List<Long> tagIdList);

    /**
     * 批量添加博文标签
     *
     * @param blogId      博文ID
     * @param blogTagList 博文标签集合
     * @return 影响行数
     */
    int batchAddBlogTag(Long blogId, List<SelectBlogTag> blogTagList);

    /**
     * 批量更新博文标签
     *
     * @param blogId      博文ID
     * @param blogTagList 博文标签集合
     * @return 影响行数
     */
    int batchUpdateBlogTag(Long blogId, List<SelectBlogTag> blogTagList);

    /**
     * 判断指定标签是否存在
     *
     * @param tagName 标签名称
     * @return 是否存在
     */
    boolean tagExistsByName(String tagName);

    /**
     * 添加一个标签
     *
     * @param tagName 标签名称
     * @return 是否成功
     */
    boolean addTag(String tagName);

    /**
     * 根据博文ID集合删除博文标签引用
     *
     * @param idList ID集合
     * @return 影响行数
     */
    long batchRemoveBlogTagByBlogIdList(List<Long> idList);
}
