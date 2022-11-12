package cn.smilex.vueblog.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author smilex
 * @date 2022/11/12/18:12
 * @since 1.0
 */
@SuppressWarnings("all")
@Configuration
public class CommonBeanConfig {

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig(
                "112233",
                1
        );
    }
}
