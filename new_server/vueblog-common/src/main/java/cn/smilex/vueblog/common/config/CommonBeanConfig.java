package cn.smilex.vueblog.common.config;

import cn.smilex.vueblog.common.entity.common.VueBlogConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author smilex
 * @date 2022/11/12/18:12
 * @since 1.0
 */
@SuppressWarnings("all")
@Slf4j
@Configuration
public class CommonBeanConfig {

    @Bean("vueBlogConfig")
    public VueBlogConfig vueBlogConfig() throws IOException {
        InputStream vueBlogConfigInputStream = null;
        String vueBlogConfigPath = System.getProperty("vueblog.config.path");

        try {
            if (StringUtils.isNoneBlank(vueBlogConfigPath)) {
                vueBlogConfigInputStream = new FileInputStream(new File(vueBlogConfigPath));
            } else {
                vueBlogConfigInputStream = CommonBeanConfig.class.getResourceAsStream("/vueblog-config.json");
            }
        } catch (Exception ignore) {
        }

        if (vueBlogConfigInputStream == null) {
            log.error("Not found \"vueblog_config.json\" file!");
            System.exit(-1);
        }

        return CommonConfig.OBJECT_MAPPER.readValue(
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
                        StringUtils.isBlank(vueBlogConfig.getSearchKey()) ? null : vueBlogConfig.getSearchKey()
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
