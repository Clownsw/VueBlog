package vip.smilex.vueblog.common.service.impl;

import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.smilex.vueblog.common.entity.blog.SearchBlogResult;
import vip.smilex.vueblog.common.entity.common.VueBlogConfig;
import vip.smilex.vueblog.common.service.SearchService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author smilex
 * @date 2022/11/19/10:06
 * @since 1.0
 */
@SuppressWarnings("unused")
@Service
public class SearchServiceImpl implements SearchService {

    private VueBlogConfig vueBlogConfig;
    private Index blogIndex;

    @Autowired
    public void setVueBlogConfig(VueBlogConfig vueBlogConfig) {
        this.vueBlogConfig = vueBlogConfig;
    }

    @Autowired
    public void setBlogIndex(Index blogIndex) {
        this.blogIndex = blogIndex;
    }

    private List<SearchBlogResult> searchResultToVec(ArrayList<HashMap<String, Object>> hitList) {
        if (hitList.size() == 0) {
            return Collections.emptyList();
        }

        List<SearchBlogResult> searchBlogResultList = new ArrayList<>(hitList.size());

        for (final HashMap<String, Object> hit : hitList) {
            searchBlogResultList.add(
                    new SearchBlogResult(
                            ((Double) hit.get("id")).longValue(),
                            (String) hit.get("title"),
                            (String) hit.get("description")
                    )
            );
        }

        return searchBlogResultList;
    }

    /**
     * 博文搜索
     *
     * @param q 关键字
     * @return 搜索结果
     */
    @Override
    public List<SearchBlogResult> search(String q) {
        try {
            final SearchResult search = blogIndex.search(
                    new SearchRequest(q)
                            .setAttributesToHighlight(vueBlogConfig.getSearchAttributesToHighlight())
                            .setLimit(vueBlogConfig.getSearchLimit())
            );

            return searchResultToVec(search.getHits());
        } catch (MeilisearchException ignore) {
        }

        return Collections.emptyList();
    }
}
