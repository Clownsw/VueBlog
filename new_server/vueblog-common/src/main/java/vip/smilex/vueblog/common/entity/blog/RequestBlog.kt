package vip.smilex.vueblog.common.entity.blog

import vip.smilex.vueblog.common.entity.tag.SelectBlogTag
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author smilex
 * @date 2023/12/4 21:56:03
 */
class RequestBlog {
    var id: Long? = null

    @JsonProperty("user_id")
    var userId: Long? = null

    @JsonProperty("sort_id")
    var sortId: Int? = null

    var title: String? = null
    var description: String? = null
    var content: String? = null
    var tag: List<vip.smilex.vueblog.common.entity.tag.SelectBlogTag>? = null
    var status: Short? = null
    var key: String? = null

    @JsonProperty("key_title")
    var keyTitle: String? = null
}