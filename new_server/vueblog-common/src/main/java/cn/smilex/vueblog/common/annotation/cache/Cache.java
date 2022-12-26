package cn.smilex.vueblog.common.annotation.cache;

import java.lang.annotation.*;

/**
 * @author smilex
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    String name() default "";
}
