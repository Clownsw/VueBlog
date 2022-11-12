package cn.smilex.vueblog.common.handler;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.DecoratingHttpServiceFunction;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServiceRequestContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author smilex
 * @date 2022/11/12/14:50
 * @since 1.0
 */
@SuppressWarnings("unused")
public class AuthService implements DecoratingHttpServiceFunction {

    @NotNull
    @Override
    public HttpResponse serve(@NotNull HttpService delegate, @NotNull ServiceRequestContext ctx, @NotNull HttpRequest req) throws Exception {
         return delegate.serve(ctx, req);
    }
}
