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
    SUCCESS(200, "success"),
    UNKNOWN_ERROR(500, "unknown error");

    final int code;
    final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
