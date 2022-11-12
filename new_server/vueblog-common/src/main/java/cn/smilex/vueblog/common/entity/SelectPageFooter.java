package cn.smilex.vueblog.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/11/12/17:37
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectPageFooter {
    private String content;

    public static SelectPageFooter fromOther(Other other) {
        return new SelectPageFooter(other.getContent());
    }
}
