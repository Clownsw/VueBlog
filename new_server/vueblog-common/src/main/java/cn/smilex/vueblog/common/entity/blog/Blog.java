package cn.smilex.vueblog.common.entity.blog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author smilex
 * @date 2022/11/12/11:24
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("m_blog")
public class Blog {
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("sort_id")
    private Integer sortId;

    @TableField("title")
    private String title;

    @TableField("description")
    private String description;

    @TableField("content")
    private String content;

    @TableField("created")
    private LocalDateTime created;

    @TableField("status")
    private Short status;

    public static Blog fromRequestBlog(RequestBlog requestBlog) {
        Blog blog = new Blog();
        blog.setId(requestBlog.getId());
        blog.setSortId(requestBlog.getSortId());
        blog.setTitle(requestBlog.getTitle());
        blog.setContent(requestBlog.getContent());
        blog.setDescription(requestBlog.getDescription());
        blog.setStatus(requestBlog.getStatus());
        return blog;
    }
}
