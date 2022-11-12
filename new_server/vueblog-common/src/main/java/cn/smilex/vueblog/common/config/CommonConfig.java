package cn.smilex.vueblog.common.config;

import org.springframework.context.ApplicationContext;

/**
 * @author smilex
 * @date 2022/11/10/21:35
 * @since 1.0
 */
public class CommonConfig {
    public static ApplicationContext APPLICATION_CONTEXT;

    static {
        APPLICATION_CONTEXT = null;
    }
}
