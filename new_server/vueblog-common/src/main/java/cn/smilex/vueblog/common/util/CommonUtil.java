package cn.smilex.vueblog.common.util;

import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.entity.common.Triplet;
import cn.smilex.vueblog.common.entity.common.Tuple;
import cn.smilex.vueblog.common.exception.VueBlogException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.common.HttpRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    /**
     * 检查请求对象中的token有效性
     *
     * @param request 请求对象
     * @return 有效性
     */
    public static Map<String, Object> checkTokenAndGetData(HttpRequest request) {
        return checkTokenAndGetData(
                request.headers()
                        .get("authorization")
        );
    }

    /**
     * 检查token有效性
     *
     * @param token token
     * @return 有效性
     */
    public static Map<String, Object> checkTokenAndGetData(String token) {
        if (StringUtils.isBlank(token)) {
            throw new VueBlogException(ResultCode.ERROR_CURRENT_LOGIN_USER_INFO_ERROR_NOT_FOUND_TOKEN);
        }

        Tuple<Boolean, Map<String, Object>> signResult = JwtUtil.signJWTToken(token);

        if (!signResult.getLeft()) {
            throw new VueBlogException(ResultCode.ERROR_CURRENT_LOGIN_USER_INFO_ERROR_NOT_FOUND_TOKEN);
        }

        return signResult.getRight();
    }

    /**
     * 计算a集合和b集合中被删除、新添加、未变动集合
     *
     * @param a   a集合
     * @param b   b集合
     * @param <T> unknown type
     * @return 集合
     */
    public static <T> Triplet<List<T>, List<T>, List<T>> getDelAndAddAndDefaultList(List<T> a, List<T> b) {
        List<T> _del = new ArrayList<>();
        List<T> _add = new ArrayList<>();
        List<T> _default = new ArrayList<>();

        // del
        for (T v : a) {
            if (!b.contains(v)) {
                _del.add(v);
            }
        }

        // add
        for (T v : b) {
            if (!a.contains(v)) {
                _add.add(v);
            }
        }

        // default
        for (T v : a) {
            if (!_del.contains(v) && !_add.contains(v) && !_default.contains(v)) {
                _default.add(v);
            }
        }

        for (T v : b) {
            if (!_del.contains(v) && !_add.contains(v) && !_default.contains(v)) {
                _default.add(v);
            }
        }

        return new Triplet<>(_del, _add, _default);
    }
}
