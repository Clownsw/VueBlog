package cn.smilex.vueblog.common.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/12/6/7:55
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Triplet<L, M, R> {
    private L left;
    private M middle;
    private R right;
}
