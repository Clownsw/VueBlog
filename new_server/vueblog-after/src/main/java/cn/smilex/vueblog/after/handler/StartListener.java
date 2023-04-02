package cn.smilex.vueblog.after.handler;

import cn.smilex.vueblog.after.controller.*;
import cn.smilex.vueblog.after.lisiener.ApplicationStartListener;
import cn.smilex.vueblog.common.handler.GlobalErrorHandler;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.linecorp.armeria.server.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author smilex
 * @date 2022/11/10/21:37
 * @since 1.0
 */
@Slf4j
@EnableTransactionManagement(proxyTargetClass = true)
@Component
public class StartListener {
    private GlobalErrorHandler globalErrorHandler;
    private LoginController loginController;
    private UserController userController;
    private BlogController blogController;
    private SortController sortController;
    private TagController tagController;
    private FriendController friendController;
    private OtherController otherController;
    private SystemController systemController;
    private MusicController musicController;
    private MonitorController monitorController;
    private ApplicationStartListener applicationStartListener;

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

    @Autowired
    public void setBlogController(BlogController blogController) {
        this.blogController = blogController;
    }

    @Autowired
    public void setSortController(SortController sortController) {
        this.sortController = sortController;
    }

    @Autowired
    public void setTagController(TagController tagController) {
        this.tagController = tagController;
    }

    @Autowired
    public void setFriendController(FriendController friendController) {
        this.friendController = friendController;
    }

    @Autowired
    public void setOtherController(OtherController otherController) {
        this.otherController = otherController;
    }

    @Autowired
    public void setSystemController(SystemController systemController) {
        this.systemController = systemController;
    }

    @Autowired
    public void setMusicController(MusicController musicController) {
        this.musicController = musicController;
    }

    @Autowired
    public void setMonitorController(MonitorController monitorController) {
        this.monitorController = monitorController;
    }

    @Autowired
    public void setApplicationStartListener(ApplicationStartListener applicationStartListener) {
        this.applicationStartListener = applicationStartListener;
    }

    public void start() {
        Server server = Server.builder()
                .http(9999)
                .errorHandler(globalErrorHandler)
                .annotatedService(loginController)
                .annotatedService(userController)
                .annotatedService(blogController)
                .annotatedService(sortController)
                .annotatedService(tagController)
                .annotatedService(friendController)
                .annotatedService(otherController)
                .annotatedService(systemController)
                .annotatedService(musicController)
                .annotatedService(monitorController)
                .build();

        // 启动时, 任务
        CommonUtil.createTask(() -> applicationStartListener.handler());

        server.start()
                .join();
    }
}
