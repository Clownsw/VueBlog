package cn.smilex.vueblog.common.function;

/**
 * @author smilex
 */
@FunctionalInterface
public interface TryRunExceptionHandler {
    void handler(Throwable e);
}
