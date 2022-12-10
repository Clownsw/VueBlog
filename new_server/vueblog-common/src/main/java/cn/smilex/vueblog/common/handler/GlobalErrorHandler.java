package cn.smilex.vueblog.common.handler;

import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.common.Result;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.common.ResponseHeaders;
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

                final int code = e.httpStatus().code();

                if (code == 404) {
                    return HttpResponse.ofJson(
                            HttpStatus.OK,
                            MediaType.JSON_UTF_8,
                            Result.fromResultCode(ResultCode.NOT_FOUND)
                    );
                }

                if (code == 405) {
                    return HttpResponse.ofJson(
                            HttpStatus.OK,
                            MediaType.JSON_UTF_8,
                            Result.fromResultCode(ResultCode.METHOD_NOT_ALLOWED)
                    );
                }
            }

            log.error("", cause);
        }

        return HttpResponse.ofJson(
                HttpStatus.OK,
                MediaType.JSON_UTF_8,
                Result.fromResultCode(ResultCode.UNKNOWN_ERROR)
        );
    }
}
