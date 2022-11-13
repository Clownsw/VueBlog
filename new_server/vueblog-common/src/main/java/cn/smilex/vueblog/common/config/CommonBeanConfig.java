package cn.smilex.vueblog.common.config;

import cn.smilex.vueblog.common.entity.VueBlogConfig;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.micrometer.core.instrument.util.IOUtils;
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
    public VueBlogConfig vueBlogConfig() throws JsonProcessingException {
        return CommonUtil.OBJECT_MAPPER.readValue(
                IOUtils.toString(CommonBeanConfig.class.getResourceAsStream("/vueblog-config.json")),
                new TypeReference<VueBlogConfig>() {
                }
        );
    }

    @Bean
    public JwtConfig jwtConfig(VueBlogConfig vueBlogConfig) {
        return new JwtConfig(
                vueBlogConfig.getJwtKey(),
                vueBlogConfig.getJwtDayNumber()
        );
    }
}
