package cn.smilex.vueblog.common.config;

import lombok.Getter;

/**
 * @author smilex
 * @date 2022/11/12/10:55
 * @since 1.0
 */
@SuppressWarnings("unused")
@Getter
public enum ResultCode {
    SUCCESS(200, "成功"),
    UNKNOWN_ERROR(500, "未知错误"),

    ERROR_REQUEST_PARAM_ERROR(1000, "错误, 请求参数错误");

    final int code;
    final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
