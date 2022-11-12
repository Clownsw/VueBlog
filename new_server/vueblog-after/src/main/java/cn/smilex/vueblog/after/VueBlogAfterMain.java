package cn.smilex.vueblog.after;

import cn.smilex.vueblog.after.handler.StartListener;
import cn.smilex.vueblog.common.config.CommonConfig;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author smilex
 * @date 2022/11/10/21:24
 * @since 1.0
 */
public class VueBlogAfterMain {
    public static void main(String[] args) {
        CommonConfig.APPLICATION_CONTEXT = new ClassPathXmlApplicationContext("/application-context.xml");
        StartListener startListener = CommonConfig.APPLICATION_CONTEXT.getBean(StartListener.class);
        startListener.start();
    }
}
