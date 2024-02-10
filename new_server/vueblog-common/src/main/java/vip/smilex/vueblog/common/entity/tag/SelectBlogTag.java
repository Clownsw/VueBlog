package vip.smilex.vueblog.common.entity.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/12/6/6:39
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectBlogTag {
    private Long id;
    private String name;
    private Long sort;
}
