package cn.smilex.vueblog.common.function;

/**
 * @author smilex
 */
@FunctionalInterface
public interface TryRunTaskNotResult {
    void run() throws Throwable;
}
