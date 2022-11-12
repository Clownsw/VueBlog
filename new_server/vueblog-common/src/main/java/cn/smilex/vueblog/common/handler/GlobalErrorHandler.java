package cn.smilex.vueblog.common.handler;

import cn.smilex.vueblog.common.entity.Result;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.common.annotation.Nullable;
import com.linecorp.armeria.server.HttpStatusException;
import com.linecorp.armeria.server.ServerErrorHandler;
import com.linecorp.armeria.server.ServiceRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author smilex
 * @date 2022/11/11/8:07
 * @since 1.0
 */
@Slf4j
@SuppressWarnings("unused")
@Component
public class GlobalErrorHandler implements ServerErrorHandler {

    @Override
    public @Nullable HttpResponse onServiceException(@Nullable ServiceRequestContext ctx, @Nullable Throwable cause) {

        if (cause != null) {
            if (cause instanceof HttpStatusException) {
                HttpStatusException e = (HttpStatusException) cause;
                if (e.httpStatus().code() == 404) {
                    return HttpResponse.ofJson(HttpStatus.OK, MediaType.JSON_UTF_8, new Result<>(404, "not found!", null));
                }
            } else {
                cause.printStackTrace();
            }
        }

        return HttpResponse.ofJson(HttpStatus.OK, MediaType.JSON_UTF_8, new Result<>(500, "unknown error!", null));
    }
}
