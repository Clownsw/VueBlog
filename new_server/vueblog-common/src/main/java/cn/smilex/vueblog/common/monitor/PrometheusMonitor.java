package cn.smilex.vueblog.common.monitor;

import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

/**
 * 普罗米修斯监控
 *
 * @author smilex
 * @date 2023/4/2/10:53
 */
@SuppressWarnings("unused")
public final class PrometheusMonitor {
    private PrometheusMonitor() {
    }

    private static final PrometheusMeterRegistry PROMETHEUS_METER_REGISTRY = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    private static final UptimeMetrics UPTIME_METRICS = new UptimeMetrics();
    private static final FileDescriptorMetrics FILE_DESCRIPTOR_METRICS = new FileDescriptorMetrics();
    private static final ProcessorMetrics PROCESSOR_METRICS = new ProcessorMetrics();
    private static final ClassLoaderMetrics CLASS_LOADER_METRICS = new ClassLoaderMetrics();
    private static final JvmMemoryMetrics JVM_MEMORY_METRICS = new JvmMemoryMetrics();
    private static final JvmHeapPressureMetrics JVM_HEAP_PRESSURE_METRICS = new JvmHeapPressureMetrics();
    private static final JvmCompilationMetrics JVM_COMPILATION_METRICS = new JvmCompilationMetrics();
    private static final JvmGcMetrics JVM_GC_METRICS = new JvmGcMetrics();
    private static final JvmInfoMetrics JVM_INFO_METRICS = new JvmInfoMetrics();
    private static final JvmThreadMetrics JVM_THREAD_METRICS = new JvmThreadMetrics();

    static {
        UPTIME_METRICS.bindTo(PROMETHEUS_METER_REGISTRY);
        FILE_DESCRIPTOR_METRICS.bindTo(PROMETHEUS_METER_REGISTRY);
        PROCESSOR_METRICS.bindTo(PROMETHEUS_METER_REGISTRY);
        CLASS_LOADER_METRICS.bindTo(PROMETHEUS_METER_REGISTRY);
        JVM_MEMORY_METRICS.bindTo(PROMETHEUS_METER_REGISTRY);
        JVM_HEAP_PRESSURE_METRICS.bindTo(PROMETHEUS_METER_REGISTRY);
        JVM_COMPILATION_METRICS.bindTo(PROMETHEUS_METER_REGISTRY);
        JVM_GC_METRICS.bindTo(PROMETHEUS_METER_REGISTRY);
        JVM_INFO_METRICS.bindTo(PROMETHEUS_METER_REGISTRY);
        JVM_THREAD_METRICS.bindTo(PROMETHEUS_METER_REGISTRY);
    }

    public static String scrape() {
        return PROMETHEUS_METER_REGISTRY.scrape();
    }
}
