package cn.smilex.vueblog.common.handler;

import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.DecoratingHttpServiceFunction;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServiceRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * @author smilex
 * @date 2022/11/12/14:50
 * @since 1.0
 */
@SuppressWarnings("unused")
@Slf4j
public class AuthService implements DecoratingHttpServiceFunction {
    @NotNull
    @Override
    public HttpResponse serve(@NotNull HttpService delegate, @NotNull ServiceRequestContext ctx, @NotNull HttpRequest req) throws Exception {
        try {
            CommonUtil.checkTokenAndGetData(req);
        } catch (Exception e) {
            return HttpResponse.ofJson(
                    HttpStatus.OK,
                    MediaType.JSON_UTF_8,
                    Result.fromResultCode(ResultCode.FORBIDDEN)
            );
        }

        return delegate.serve(ctx, req);
    }
}
