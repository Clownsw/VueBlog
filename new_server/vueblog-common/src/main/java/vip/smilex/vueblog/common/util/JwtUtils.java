package vip.smilex.vueblog.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import vip.smilex.vueblog.common.config.CommonConfig;
import vip.smilex.vueblog.common.config.JwtConfig;
import vip.smilex.vueblog.common.entity.common.Tuple;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author smilex
 * @date 2022/9/29/17:24
 * @since 1.0
 */
@Slf4j
public final class JwtUtils {
    private static JwtConfig JWT_CONFIG;

    public static JwtConfig getJwtConfig() {
        if (JWT_CONFIG == null) {
            JWT_CONFIG = CommonConfig.APPLICATION_CONTEXT
                    .getBean(JwtConfig.class);
            JWT_CONFIG.setAlgorithm(Algorithm.HMAC256(JWT_CONFIG.getKey()));
        }
        return JWT_CONFIG;
    }

    public static String createJWTToken(HashMap<String, Object> data) {
        return JWT.create()
                .withClaim("data", data)
                .withExpiresAt(
                        Date.from(
                                LocalDateTime.now()
                                        .plusDays(getJwtConfig().getDayNumber())
                                        .atZone(ZoneId.systemDefault())
                                        .toInstant()
                        )
                )
                .sign(getJwtConfig().getAlgorithm());
    }

    public static Tuple<Boolean, Map<String, Object>> signJWTToken(String token) {
        try {
            Map<String, Object> data = JWT.require(getJwtConfig().getAlgorithm())
                    .build()
                    .verify(token)
                    .getClaim("data")
                    .asMap();
            return new Tuple<>(true, data);
        } catch (Exception e) {
            log.error("", e);
        }
        return new Tuple<>(false, null);
    }
}
