package cn.smilex.vueblog.common.entity.blog

/**
 * @author smilex
 * @date 2023/12/4 22:00:52
 */
data class SearchBlog(
    var id: Long? = null,
    var title: String? = null,
    var description: String? = null,
    var content: String? = null
) {
    companion object {
        @JvmStatic
        fun fromRequestBlog(requestBlog: RequestBlog): SearchBlog {
            val searchBlog = SearchBlog()

            searchBlog.id = requestBlog.id
            searchBlog.title = requestBlog.title
            searchBlog.description = requestBlog.description
            searchBlog.content = requestBlog.content

            return searchBlog
        }

        @JvmStatic
        fun fromBlog(blog: Blog): SearchBlog {
            return SearchBlog(
                blog.id,
                blog.title,
                blog.description,
                blog.content
            )
        }
    }
}