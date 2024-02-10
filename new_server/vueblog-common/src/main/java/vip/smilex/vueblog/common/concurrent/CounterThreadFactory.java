package vip.smilex.vueblog.common.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author smilex
 */
public class CounterThreadFactory implements ThreadFactory {
    private final String threadFactoryName;
    private final LongAdder counter;

    public CounterThreadFactory(String threadFactoryName) {
        this.threadFactoryName = threadFactoryName;
        this.counter = new LongAdder();
    }

    @Override
    public Thread newThread(@NotNull Runnable runnable) {
        counter.increment();
        return new Thread(runnable, String.format("%s-%s", this.threadFactoryName, counter.longValue()));
    }
}
