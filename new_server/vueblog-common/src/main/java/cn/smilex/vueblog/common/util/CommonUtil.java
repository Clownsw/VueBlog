package cn.smilex.vueblog.common.util;

/**
 * @author smilex
 * @date 2022/9/29/18:09
 * @since 1.0
 */
public final class CommonUtil {
    public static final String EMPTY_STRING = "";

    public static boolean isInForArray(Class<?> clazz, Class<?>[] array) {
        for (Class<?> aClass : array) {
            if (aClass.equals(clazz)) {
                return true;
            }
        }
        return false;
    }
}
