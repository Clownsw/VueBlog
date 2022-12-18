package cn.smilex.vueblog.before.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.service.SearchService;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.PathPrefix;
import com.linecorp.armeria.server.annotation.ProducesJson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author smilex
 * @date 2022/11/19/10:05
 * @since 1.0
 */
@SuppressWarnings("unused")
@PathPrefix("/search")
@ProducesJson
@CrossOrigin
@Component
public class SearchController {
    private SearchService searchService;

    @Autowired
    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * 博文搜索
     *
     * @param q 关键字
     * @return 搜索结果
     */
    @Get("")
    public Result<?> search(@Param("q") Optional<String> q) {
        if (!q.isPresent() || StringUtils.isBlank(q.get())) {
            return Result.error();
        }
        return Result.success(searchService.search(q.get()));
    }
}
