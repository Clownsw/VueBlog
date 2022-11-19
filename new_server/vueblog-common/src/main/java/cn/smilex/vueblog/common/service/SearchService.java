package cn.smilex.vueblog.common.service;

import cn.smilex.vueblog.common.entity.SearchBlogResult;

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
