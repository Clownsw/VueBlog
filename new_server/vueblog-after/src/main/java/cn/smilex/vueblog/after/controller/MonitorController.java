package cn.smilex.vueblog.after.controller;

import cn.smilex.vueblog.common.annotation.CrossOrigin;
import cn.smilex.vueblog.common.monitor.PrometheusMonitor;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Options;
import com.linecorp.armeria.server.annotation.ProducesText;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 监控控制器
 *
 * @author smilex
 * @date 2023/4/2/21:59
 * @since 1.0
 */
@SuppressWarnings("unused")
@Slf4j
@ProducesText
@CrossOrigin
@Component
public class MonitorController {

    /**
     * 获取普罗米修斯监控数据
     *
     * @return 监控数据
     */
    @Get("/prometheus")
    @Options("/prometheus")
    public String prometheus() {
        return PrometheusMonitor.scrape();
    }
}
