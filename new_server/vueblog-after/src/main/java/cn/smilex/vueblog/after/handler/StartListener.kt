package cn.smilex.vueblog.after.handler

import cn.smilex.vueblog.after.controller.*
import cn.smilex.vueblog.common.handler.GlobalErrorHandler
import com.linecorp.armeria.server.Server
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * @author smilex
 * @date 2023/12/3 17:33:34
 */
@Suppress("unused")
@EnableTransactionManagement(proxyTargetClass = true)
@Component
class StartListener(
    private val globalErrorHandler: GlobalErrorHandler,
    private val loginController: LoginController,
    private val userController: UserController,
    private val blogController: BlogController,
    private val sortController: SortController,
    private val tagController: TagController,
    private val friendController: FriendController,
    private val otherController: OtherController,
    private val systemController: SystemController,
    private val musicController: MusicController
) : InitializingBean {
    override fun afterPropertiesSet() {
        Server.builder()
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
            .build()
            .start()
            .join()
    }
}