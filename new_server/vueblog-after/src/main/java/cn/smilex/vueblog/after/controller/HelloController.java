package cn.smilex.vueblog.after.controller;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.annotation.Get;
import org.springframework.stereotype.Component;

/**
 * @author smilex
 * @date 2022/11/12/10:32
 * @since 1.0
 */
@SuppressWarnings("unused")
@Component
public class HelloController {

    @Get("/hello")
    public HttpResponse hello() {
        return HttpResponse.of(HttpStatus.OK, MediaType.PLAIN_TEXT_UTF_8, "hello");
    }
}
