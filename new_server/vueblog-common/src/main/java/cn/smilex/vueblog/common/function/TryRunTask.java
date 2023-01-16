package cn.smilex.vueblog.common.function;

/**
 * @author smilex
 */
@FunctionalInterface
public interface TryRunTask<T> {
    T run() throws Throwable;
}
