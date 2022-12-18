package cn.smilex.vueblog.common.handler;

import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.linecorp.armeria.common.HttpResponse;
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

                switch (code) {
                    case 403: {
                        return CommonUtil.buildJsonHttpResponseByResultCode(ResultCode.FORBIDDEN);
                    }

                    case 404: {
                        return CommonUtil.buildJsonHttpResponseByResultCode(ResultCode.NOT_FOUND);
                    }

                    case 405: {
                        return CommonUtil.buildJsonHttpResponseByResultCode(ResultCode.METHOD_NOT_ALLOWED);
                    }
                }
            }

            log.error("", cause);
        }

        return CommonUtil.buildJsonHttpResponseByResultCode(ResultCode.UNKNOWN_ERROR);
    }
}
