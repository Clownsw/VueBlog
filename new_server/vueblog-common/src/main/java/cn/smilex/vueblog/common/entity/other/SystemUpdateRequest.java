package cn.smilex.vueblog.common.entity.other;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/12/18/14:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemUpdateRequest {
    private System system;
    private Footer footer;
}
