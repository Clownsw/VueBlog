package vip.smilex.vueblog.before.handler

import com.linecorp.armeria.server.Server
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.EnableTransactionManagement
import vip.smilex.vueblog.before.controller.*
import vip.smilex.vueblog.common.handler.GlobalErrorHandler

/**
 * @author smilex
 * @date 2023/12/3 17:28:36
 */
@Suppress("unused")
@EnableTransactionManagement(proxyTargetClass = true)
@Component
class StartListener(
    private val globalErrorHandler: GlobalErrorHandler,
    private val blogController: BlogController,
    private val sortController: SortController,
    private val tagController: TagController,
    private val friendController: FriendController,
    private val systemController: SystemController,
    private val otherController: OtherController,
    private val searchController: SearchController,
    private val musicController: MusicController
) : InitializingBean {
    override fun afterPropertiesSet() {
        Server.builder()
            .http(8888)
            .errorHandler(globalErrorHandler)
            .annotatedService(blogController)
            .annotatedService(sortController)
            .annotatedService(tagController)
            .annotatedService(friendController)
            .annotatedService(systemController)
            .annotatedService(otherController)
            .annotatedService(searchController)
            .annotatedService(musicController)
            .build()
            .start()
            .join()
    }
}