package vip.smilex.vueblog.common.entity.common;

import vip.smilex.vueblog.common.entity.other.Other;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vip.smilex.vueblog.common.entity.other.Other;

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
