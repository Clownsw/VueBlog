package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.TagDao;
import cn.smilex.vueblog.common.entity.tag.Tag;
import cn.smilex.vueblog.common.service.TagService;
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
    public List<Tag> selectTagByBlogId(Long blogId) {
        return this.getBaseMapper()
                .selectTagByBlogId(blogId);
    }
}
