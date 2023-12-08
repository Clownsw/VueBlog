package cn.smilex.vueblog.common.handler;

import cn.smilex.vueblog.common.annotation.cache.Cache;
import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.util.CommonUtils;
import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.DecoratingHttpServiceFunction;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServiceRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Objects;

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

        if (req.method() == HttpMethod.OPTIONS) {
            return Objects.requireNonNull(CommonUtils.createCrossOriginHttpResponse());
        }

        try {
            CommonUtils.checkTokenAndGetData(req);
        } catch (Exception e) {
            return CommonUtils.buildJsonHttpResponseByResultCode(ResultCode.FORBIDDEN);
        }

        Method method = CommonUtils.getHttpServiceMethodField(delegate);

        if (method != null) {
            Cache cacheAnnotation = method.getAnnotation(Cache.class);
        }

        return delegate.serve(ctx, req);
    }
}
