package vip.smilex.vueblog.common.config;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/11/12/18:11
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtConfig {
    private String key;
    private Integer dayNumber;
    private Algorithm algorithm;

    public JwtConfig(String key, Integer dayNumber) {
        this.key = key;
        this.dayNumber = dayNumber;
    }
}
