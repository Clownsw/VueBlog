package cn.smilex.vueblog.after.handler;

import cn.smilex.vueblog.after.controller.LoginController;
import cn.smilex.vueblog.after.controller.UserController;
import cn.smilex.vueblog.common.handler.GlobalErrorHandler;
import com.linecorp.armeria.server.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author smilex
 * @date 2022/11/10/21:37
 * @since 1.0
 */
@Slf4j
@Component
public class StartListener {
    private GlobalErrorHandler globalErrorHandler;
    private LoginController loginController;
    private UserController userController;

    @Autowired
    public void setGlobalErrorHandler(GlobalErrorHandler globalErrorHandler) {
        this.globalErrorHandler = globalErrorHandler;
    }

    @Autowired
    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    @Autowired
    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void start() {
        Server server = Server.builder()
                .http(9999)
                .errorHandler(globalErrorHandler)
                .annotatedService(loginController)
                .annotatedService(userController)
                .build();

        server.start()
                .join();
    }
}
