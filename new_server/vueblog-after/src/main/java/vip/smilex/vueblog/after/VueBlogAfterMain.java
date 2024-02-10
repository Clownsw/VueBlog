package vip.smilex.vueblog.after;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import vip.smilex.vueblog.common.config.CommonConfig;

/**
 * @author smilex
 * @date 2022/11/10/21:24
 * @since 1.0
 */
public class VueBlogAfterMain {
    public static void main(String[] args) {
        CommonConfig.APPLICATION_CONTEXT = new ClassPathXmlApplicationContext("/application-context.xml");
    }
}
