package cn.smilex.vueblog.common.entity.common

/**
 * TODO
 * @author smilex
 * @date 2023/12/4 21:58:50
 */
data class Triplet<L, M, R>(
    var left: L? = null,
    var middle: M? = null,
    var right: R? = null
)