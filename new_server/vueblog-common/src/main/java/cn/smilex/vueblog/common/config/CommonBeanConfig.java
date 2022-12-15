package cn.smilex.vueblog.common.config;

import cn.smilex.vueblog.common.entity.common.VueBlogConfig;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import io.micrometer.core.instrument.util.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author smilex
 * @date 2022/11/12/18:12
 * @since 1.0
 */
@SuppressWarnings("all")
@Configuration
public class CommonBeanConfig {

    @Bean("vueBlogConfig")
    public VueBlogConfig vueBlogConfig() throws JsonProcessingException {
        InputStream vueBlogConfigInputStream;
        String vueBlogConfigPath = System.getProperty("vueblog.config.path");

        if (StringUtils.isNoneBlank(vueBlogConfigPath)) {
            try {
                vueBlogConfigInputStream = new FileInputStream(new File(vueBlogConfigPath));
            } catch (FileNotFoundException ignore) {
                vueBlogConfigInputStream = null;
            }
        } else {
            vueBlogConfigInputStream = CommonBeanConfig.class.getResourceAsStream("/vueblog-config.json");
        }

        return CommonUtil.OBJECT_MAPPER.readValue(
                IOUtils.toString(vueBlogConfigInputStream),
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

    @Bean("client")
    public Client meilisearchClient(VueBlogConfig vueBlogConfig) {
        return new Client(
                new Config(
                        vueBlogConfig.getSearchServer(),
                        vueBlogConfig.getSearchKey()
                )
        );
    }

    @Bean
    @DependsOn("client")
    public Index blogIndex(Client client) throws MeilisearchException {
        return client.index("blog");
    }

    @Bean
    public TransactionManager transactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }
}
