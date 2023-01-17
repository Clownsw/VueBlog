package cn.smilex.vueblog.common.config;

import cn.smilex.vueblog.common.concurrent.CounterThreadFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.ApplicationContext;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author smilex
 * @date 2022/11/10/21:35
 * @since 1.0
 */
@SuppressWarnings("all")
public class CommonConfig {
    public static ApplicationContext APPLICATION_CONTEXT = null;
    public static final int VUEBLOG_BLOG_KEY_SIZE = 30;
    public static final String SEARCH_DOCUMENT_PRIMARY_KEY = "id";

    public static final String EMPTY_STRING = "";
    public static final Map<?, ?> EMPTY_MAP = new HashMap<>(0);
    public static final List<?> EMPTY_LIST = new ArrayList<>(0);
    public static final Deque<?> EMTPY_DEQUE = new LinkedBlockingDeque<>();
    public static final String EMPTY_FRIEND_MESSAGE = "暂无友链";


    public static final String COMMA = ",";
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final ExecutorService COMMON_THREAD_POOL = Executors.newCachedThreadPool(
            new CounterThreadFactory("common-pool")
    );

    static {
        OBJECT_MAPPER.registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    public static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    public static final String MUSIC_API_SONG_URL_TEMPLATE = "%s/vueblog/song/url?id=%d";
    public static final String MUSIC_API_LRC_TEMPLATE = "%s/vueblog/lyric?id=%d";
}
