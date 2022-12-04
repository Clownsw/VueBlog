package cn.smilex.vueblog.common.annotation;

import com.linecorp.armeria.server.annotation.AdditionalHeader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author smilex
 * @date 2022/11/12/17:20
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@AdditionalHeader(name = "Access-Control-Allow-Origin", value = "*")
@AdditionalHeader(name = "Access-Control-Allow-Credentials", value = "true")
@AdditionalHeader(name = "Access-Control-Allow-Methods", value = "*")
@AdditionalHeader(name = "Access-Control-Allow-Headers", value = "*")
@AdditionalHeader(name = "Access-Control-Expose-Headers", value = "*")
public @interface CrossOrigin {
}
