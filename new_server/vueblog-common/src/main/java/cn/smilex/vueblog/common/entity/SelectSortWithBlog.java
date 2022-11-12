package cn.smilex.vueblog.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/11/12/18:39
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectSortWithBlog {
    private Integer id;
    private Integer order;
    private String name;
}
