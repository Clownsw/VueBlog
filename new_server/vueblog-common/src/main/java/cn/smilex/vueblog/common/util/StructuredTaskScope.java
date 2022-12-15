package cn.smilex.vueblog.common.util;

import com.zaxxer.hikari.util.FastList;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author smilex
 * @date 2022/11/12/19:01
 * @since 1.0
 */
@SuppressWarnings("unused")
@Slf4j
public final class StructuredTaskScope implements AutoCloseable {
    private final List<Future<?>> futureList;

    public StructuredTaskScope() {
        this.futureList = new FastList<>(Future.class);
    }

    public StructuredTaskScope(int capacity) {
        this.futureList = new FastList<>(Future.class, capacity);
    }

    public void execute(Runnable runnable) {
        this.futureList.add(CommonUtil.createTask(runnable));
    }

    @Override
    public void close() {
        for (Future<?> future : futureList) {
            try {
                future.get();
            } catch (Exception ignore) {
            }
        }

        this.futureList.clear();
    }
}
