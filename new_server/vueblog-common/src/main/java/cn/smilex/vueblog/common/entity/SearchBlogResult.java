package cn.smilex.vueblog.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/11/19/10:38
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchBlogResult {
    private Long id;
    private String title;
    private String description;
}
