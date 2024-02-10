package vip.smilex.vueblog.common.config

import vip.smilex.vueblog.common.concurrent.CounterThreadFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.support.ClassPathXmlApplicationContext
import java.lang.invoke.MethodHandles
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingDeque

/**
 * @author yanglujia
 * @date 2023/12/8 15:27:43
 */
class CommonConfig {
    companion object {
        lateinit var APPLICATION_CONTEXT: ClassPathXmlApplicationContext

        @JvmField
        var OBJECT_MAPPER: ObjectMapper = ObjectMapper()

        const val VUEBLOG_BLOG_KEY_SIZE = 30
        const val SEARCH_DOCUMENT_PRIMARY_KEY = "id"

        const val EMPTY_STRING = ""

        private val EMTPY_DEQUE: Deque<*> = LinkedBlockingDeque<Any>()

        const val EMPTY_FRIEND_MESSAGE = "暂无友链"

        const val COMMA = ","

        @JvmField
        val COMMON_THREAD_POOL: ExecutorService = Executors.newCachedThreadPool(
            vip.smilex.vueblog.common.concurrent.CounterThreadFactory("common-pool")
        )

        @JvmField
        val LOOKUP: MethodHandles.Lookup = MethodHandles.lookup()

        const val MUSIC_API_SONG_URL_TEMPLATE = "%s/vueblog/song/url?id=%d"
        const val MUSIC_API_LRC_TEMPLATE = "%s/vueblog/lyric?id=%d"
        const val MUSIC_API_PLAY_LIST_DETAIL_TEMPLATE = "%s/vueblog/playlist/detail?id=%d"

        init {
            OBJECT_MAPPER.registerModule(Jdk8Module())
                .registerModule(JavaTimeModule())
                .registerModule(KotlinModule())
        }

        @JvmStatic
        fun<T> emptyDeque(): Deque<T> {
            @Suppress("UNCHECKED_CAST")
            return EMTPY_DEQUE as Deque<T>
        }
    }
}