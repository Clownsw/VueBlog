package cn.smilex.vueblog.common.entity.blog;

import cn.smilex.vueblog.common.entity.tag.SelectBlogTag;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author smilex
 * @date 2022/12/6/6:34
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestBlog {
    private Long id;

    @JsonAlias("user_id")
    private Long userId;

    @JsonAlias("sort_id")
    private Integer sortId;

    private String title;
    private String description;
    private String content;
    private List<SelectBlogTag> tag;
    private Short status;
    private String key;

    @JsonAlias("key_title")
    private String keyTitle;
}
