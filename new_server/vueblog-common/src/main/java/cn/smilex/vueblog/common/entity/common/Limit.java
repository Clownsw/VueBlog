package cn.smilex.vueblog.common.entity.common;

import cn.smilex.vueblog.common.config.CommonConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * @author smilex
 * @date 2022/11/13/13:34
 * @since 1.0
 */
@SuppressWarnings("unchecked")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Limit<T> {
    private List<T> dataList;
    private Long totalCount;
    private Long pageSize;
    private Long currentPage;
    private Long pageCount;

    public static <T> Limit<T> defaultLimit(Long pageSize, Long currentPage) {
        return new Limit<>(
                Collections.emptyList(),
                0L,
                pageSize,
                currentPage,
                0L
        );
    }
}
