package vip.smilex.vueblog.common.entity.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/11/13/21:23
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SelectBlogInfo extends SelectShowBlog {
    private String content;
}
