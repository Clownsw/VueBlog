package cn.smilex.vueblog.common.service;

import cn.smilex.vueblog.common.entity.tag.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author smilex
 * @date 2022/11/12/11:45
 * @since 1.0
 */
@SuppressWarnings("unused")
public interface TagService extends IService<Tag> {

    /**
     * 根据博文ID查询所有标签
     *
     * @param blogId 博文ID
     * @return 所有标签
     */
    List<Tag> selectTagByBlogId(Long blogId);
}
