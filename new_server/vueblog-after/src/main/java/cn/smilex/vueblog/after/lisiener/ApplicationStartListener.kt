package cn.smilex.vueblog.after.lisiener

import cn.smilex.vueblog.common.config.CommonConfig
import com.meilisearch.sdk.Client
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

/**
 * @author smilex
 * @date 2023/12/3 17:31:46
 */
@Suppress("unused")
@Component
class ApplicationStartListener(private val client: Client) : InitializingBean {
    companion object {
        @JvmStatic
        val LOGGER: Logger = LoggerFactory.getLogger(ApplicationStartListener::class.java)
    }

    override fun afterPropertiesSet() {
        try {
            client.createIndex("blog")
        } catch (e: Exception) {
            if (LOGGER.isErrorEnabled) {
                LOGGER.error(CommonConfig.EMPTY_STRING, e)
            }
        }
    }
}