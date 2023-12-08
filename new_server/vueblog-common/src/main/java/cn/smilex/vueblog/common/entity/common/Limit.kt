package cn.smilex.vueblog.common.entity.common

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @author smilex
 * @date 2023/12/8 13:58:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class Limit<T>(
    var dataList: List<T>,
    var totalCount: Long,
    var pageSize: Long,
    var currentPage: Long,
    var pageCount: Long
) {
    companion object {
        @JvmStatic
        fun <T> defaultLimit(pageSize: Long, currentPage: Long): Limit<T> {
            return Limit(
                emptyList(),
                0L,
                pageSize,
                currentPage,
                0L
            )
        }
    }
}