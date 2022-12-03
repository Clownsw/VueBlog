package cn.smilex.vueblog.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.concurrent.Future;

/**
 * @author smilex
 * @date 2022/11/12/19:01
 * @since 1.0
 */
@Slf4j
public final class StructuredTaskScope implements AutoCloseable {
    private final LinkedList<Future<?>> futureList;

    public StructuredTaskScope() {
        this.futureList = new LinkedList<>();
    }

    public void execute(Runnable runnable) {
        this.futureList.add(CommonUtil.submitToThreadPool(runnable));
    }

    @Override
    public void close() {
        for (Future<?> future : futureList) {
            try {
                future.get();
            } catch (Exception ignore) {
            }
        }
    }
}
