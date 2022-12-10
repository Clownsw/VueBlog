package cn.smilex.vueblog.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * @author smilex
 * @date 2022/11/12/19:01
 * @since 1.0
 */
@Slf4j
public final class StructuredTaskScope implements AutoCloseable {
    private final LinkedList<Thread> threadList;

    public StructuredTaskScope() {
        this.threadList = new LinkedList<>();
    }

    public void execute(Runnable runnable) {
        this.threadList.add(Thread.startVirtualThread(runnable));
    }

    @Override
    public void close() {
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (Exception ignore) {
            }
        }
    }
}
