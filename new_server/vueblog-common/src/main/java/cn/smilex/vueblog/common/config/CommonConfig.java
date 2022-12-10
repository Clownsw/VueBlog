package cn.smilex.vueblog.common.config;

import org.springframework.context.ApplicationContext;

/**
 * @author smilex
 * @date 2022/11/10/21:35
 * @since 1.0
 */
@SuppressWarnings("all")
public class CommonConfig {
    public static ApplicationContext APPLICATION_CONTEXT;
    public static final int VUEBLOG_BLOG_KEY_SIZE = 30;
    public static final String SEARCH_DOCUMENT_PRIMARY_KEY = "id";

    static {
        APPLICATION_CONTEXT = null;
    }
}
