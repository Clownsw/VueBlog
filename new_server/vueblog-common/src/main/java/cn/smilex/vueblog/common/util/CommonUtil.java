package cn.smilex.vueblog.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author smilex
 * @date 2022/9/29/18:09
 * @since 1.0
 */
public final class CommonUtil {
    public static final String EMPTY_STRING = "";
    public static final String EMPTY_FRIEND_MESSAGE = "暂无友链";
    public static final String COMMA = ",";
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static boolean isInForArray(Class<?> clazz, Class<?>[] array) {
        for (Class<?> aClass : array) {
            if (aClass.equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据每页数量查询起始查询位置
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 起始查询位置
     */
    public static long calcLimit(long currentPage, long pageSize) {
        return (currentPage - 1L) * pageSize;
    }

    /**
     * 根据总数量和每页数量计算总页数
     *
     * @param totalCount 总数量
     * @param pageSize   每页数量
     * @return 总页数
     */
    public static long calcPageCount(long totalCount, long pageSize) {
        long pageCount = 0;

        if (totalCount > 0) {
            if (totalCount <= pageSize) {
                pageCount = 1;
            } else if (totalCount % pageSize == 0) {
                pageCount = totalCount / pageSize;
            } else {
                pageCount = (totalCount / pageSize) + 1;
            }
        }

        return pageCount;
    }
}
