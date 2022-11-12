package cn.smilex.vueblog.after.handler;

import cn.smilex.vueblog.after.controller.HelloController;
import cn.smilex.vueblog.common.handler.GlobalErrorHandler;
import com.linecorp.armeria.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author smilex
 * @date 2022/11/10/21:37
 * @since 1.0
 */
@Component
public class StartListener {
    private GlobalErrorHandler globalErrorHandler;
    private HelloController helloController;

    @Autowired
    public void setGlobalErrorHandler(GlobalErrorHandler globalErrorHandler) {
        this.globalErrorHandler = globalErrorHandler;
    }

    @Autowired
    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    public void start() {
        Server server = Server.builder()
                .http(2222)
                .errorHandler(globalErrorHandler)
                .annotatedService(helloController)
                .build();

        server.start()
                .join();
    }
}
