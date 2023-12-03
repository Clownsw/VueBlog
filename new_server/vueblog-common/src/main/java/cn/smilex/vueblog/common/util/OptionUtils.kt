package cn.smilex.vueblog.common.util

import java.util.*

/**
 * TODO
 * @author smilex
 * @date 2023/12/3 15:37:45
 */
class OptionUtils {
    companion object {

        /**
         * 是否为空
         *
         * @param value Optional实例
         */
        @JvmStatic
        fun <T> isEmpty(value: Optional<T>?): Boolean {
            return value?.get() == null;
        }

        /**
         * 如果为空则执行a阶段反之执行b阶段
         *
         * @param value Optional实例
         * @param isEmptyCallBack 对Optional实例判定是否为空
         * @param a 为空阶段
         * @param b 不为空阶段
         */
        @JvmStatic
        inline fun <T, R> isEmptyAction(
            value: Optional<T>,
            isEmptyCallBack: (Optional<T>?) -> Boolean,
            a: () -> R,
            b: (T) -> R
        ): R {
            if (isEmptyCallBack(value)) {
                return a();
            }

            return b(value.get())
        }
    }
}