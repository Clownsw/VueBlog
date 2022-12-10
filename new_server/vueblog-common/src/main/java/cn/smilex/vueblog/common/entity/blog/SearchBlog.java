package cn.smilex.vueblog.common.entity.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/12/10/18:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchBlog {
    private Long id;
    private String title;
    private String description;
    private String content;

    public static SearchBlog fromRequestBlog(RequestBlog requestBlog) {
        SearchBlog searchBlog = new SearchBlog();
        searchBlog.setId(requestBlog.getId());
        searchBlog.setTitle(requestBlog.getTitle());
        searchBlog.setDescription(requestBlog.getDescription());
        searchBlog.setContent(requestBlog.getContent());
        return searchBlog;
    }
}
