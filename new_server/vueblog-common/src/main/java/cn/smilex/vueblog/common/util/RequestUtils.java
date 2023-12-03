package cn.smilex.vueblog.common.util;

import cn.smilex.req.HttpBodyInterface;
import cn.smilex.req.HttpRequest;
import cn.smilex.req.HttpResponse;
import cn.smilex.req.Requests;
import cn.smilex.vueblog.common.entity.common.HashMapBuilder;

import java.util.HashMap;
import java.util.Optional;

import static cn.smilex.req.Requests.REQUEST_METHOD.GET;

/**
 * @author smilex
 * @date 2022/9/29/13:02
 * @since 1.0
 */
public final class RequestUtils {
    public static HashMap<String, String> JSON_HEADER = new HashMapBuilder<String, String>(1)
            .put("content-type", "application/json")
            .getMap();

    public static HttpResponse get(
            String url
    ) {
        return Requests.requests.fast_get(url);
    }

    public static Optional<HttpResponse> get(
            String url,
            HashMap<String, String> headers,
            HashMap<String, String> cookies
    ) {
        return req(GET, url, headers, cookies, null);
    }

    public static Optional<HttpResponse> req(
            Requests.REQUEST_METHOD method,
            String url,
            HashMap<String, String> headers,
            HttpBodyInterface body
    ) {
        return req(method, url, headers, new HashMap<>(0), body);
    }

    public static Optional<HttpResponse> req(
            Requests.REQUEST_METHOD method,
            String url,
            HashMap<String, String> headers,
            HashMap<String, String> cookies,
            HttpBodyInterface body
    ) {
        return Optional.of(
                Requests.requests.request(
                        HttpRequest.build()
                                .setUrl(url)
                                .setMethod(method)
                                .setHeaders(headers)
                                .setCookies(cookies)
                                .setBody(body)
                )
        );
    }
}
