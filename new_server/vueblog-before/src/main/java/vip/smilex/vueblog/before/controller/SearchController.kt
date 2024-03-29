package vip.smilex.vueblog.before.controller

import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.PathPrefix
import com.linecorp.armeria.server.annotation.ProducesJson
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import vip.smilex.vueblog.common.annotation.CrossOrigin
import vip.smilex.vueblog.common.entity.common.Result
import vip.smilex.vueblog.common.service.SearchService
import vip.smilex.vueblog.common.util.OptionUtils
import vip.smilex.vueblog.common.util.OptionUtils.Companion.isEmptyAction
import java.util.*

/**
 * @author smilex
 * @date 2023/12/3 17:22:12
 */
@PathPrefix("/search")
@ProducesJson
@CrossOrigin
@Component
class SearchController(private val searchService: SearchService) {
    /**
     * 博文搜索
     *
     * @param q 关键字
     * @return 搜索结果
     */
    @Get("")
    fun search(
        @Param("q") q: Optional<String>
    ): Result<*> {
        return isEmptyAction(
            q,
            { q1: Optional<String>? ->
                OptionUtils.isEmpty(q1) || StringUtils.isBlank(
                    q1!!.get()
                )
            },
            { Result.error() },
            { q1: String? ->
                Result.success(
                    searchService.search(q1)
                )
            }
        )
    }
}