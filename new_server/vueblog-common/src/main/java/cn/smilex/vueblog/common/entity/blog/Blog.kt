package cn.smilex.vueblog.common.entity.blog

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.LocalDateTime

/**
 * @author smilex
 * @date 2023/12/4 21:39:49
 */
data class Blog(
    @TableId(value = "id", type = IdType.AUTO) @JsonSerialize(using = ToStringSerializer::class)
    var id: Long? = null,

    @TableField("user_id")
    var userId: Long? = null,

    @TableField("sort_id")
    var sortId: Int? = null,

    @TableField("title")
    var title: String? = null,

    @TableField("description")
    var description: String? = null,

    @TableField("content")
    var content: String? = null,

    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    @TableField("created")
    var created: LocalDateTime? = null,

    @TableField("status")
    var status: Short? = null
) {
    companion object {
        @JvmStatic
        fun fromRequestBlog(requestBlog: RequestBlog): Blog {
            val blog = Blog()

            blog.id = requestBlog.id
            blog.sortId = requestBlog.sortId
            blog.title = requestBlog.title
            blog.content = requestBlog.content
            blog.description = requestBlog.description
            blog.status = requestBlog.status
            return blog
        }

        @JvmStatic
        fun copyFromRequestBlog(requestBlog: RequestBlog): Blog? {
            val blog = fromRequestBlog(requestBlog)

            blog.userId = requestBlog.userId
            blog.created = LocalDateTime.now()

            return blog
        }
    }
}
