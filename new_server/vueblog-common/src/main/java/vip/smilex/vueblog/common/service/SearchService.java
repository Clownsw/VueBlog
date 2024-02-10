package vip.smilex.vueblog.common.service;

import vip.smilex.vueblog.common.entity.blog.SearchBlogResult;
import vip.smilex.vueblog.common.entity.blog.SearchBlogResult;

import java.util.List;

/**
 * @author smilex
 * @date 2022/11/19/10:06
 * @since 1.0
 */
public interface SearchService {

    /**
     * 博文搜索
     *
     * @param q 关键字
     * @return 搜索结果
     */
    List<SearchBlogResult> search(String q);
}
