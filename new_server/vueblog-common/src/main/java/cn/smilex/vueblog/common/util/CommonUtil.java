package cn.smilex.vueblog.common.util;

import cn.smilex.vueblog.common.entity.common.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    public static Future<?> submitToThreadPool(Runnable runnable) {
        return THREAD_POOL.submit(runnable);
    }

    public static Future<?> submitToThreadPool(Callable<?> callable) {
        return THREAD_POOL.submit(callable);
    }

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

    /**
     * object集合检查存在null
     *
     * @param objectList object集合
     * @return 检查存在null
     */
    public static boolean objectListCheckHaveNull(Object... objectList) {
        for (Object object : objectList) {
            if (object == null) {
                return true;
            }

            if (object instanceof String) {
                if (StringUtils.isBlank((String) object)) {
                    return true;
                }
            }

            if (object instanceof Optional) {
                if (!((Optional<?>) object).isPresent()) {
                    return true;
                }
            }
        }

        return false;
    }

    @FunctionalInterface
    public interface TryRunTask<T> {
        T run();
    }

    @FunctionalInterface
    public interface TryRunExceptionHandler<T> {
        Optional<T> handler(Throwable e);
    }

    /**
     * try catch 运行 task, 如果发生异常 则调用exceptionHandler进行异常处理
     *
     * @param task                   任务
     * @param tryRunExceptionHandler 异常处理
     * @param <T>                    unknown type1
     * @param <K>                    unknown type2
     * @return 结果
     */
    @SuppressWarnings("all")
    public static <T, K> Result<?> tryRun(TryRunTask<T> task, Optional<TryRunExceptionHandler<K>> exceptionHandler) {
        try {
            return Result.success(task.run());
        } catch (Exception e) {
            if (exceptionHandler.isPresent()) {
                Optional<K> result = exceptionHandler.get().handler(e);

                if (result.isPresent()) {
                    return Result.error(result.get());
                }
            }
            return Result.error();
        }
    }
}
