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

    ERROR_REQUEST_PARAM_ERROR(1000, "错误, 请求参数错误"),
    ERROR_LOGIN_NOT_FOUND_USER(2000, "错误, 登录失败未查询到该用户"),
    ERROR_LOGIN_PASSWORD_ERROR(2001, "错误, 登录失败密码错误"),
    ERROR_CURRENT_LOGIN_USER_INFO_ERROR_NOT_FOUND_TOKEN(2002, "错误, 获取当前登录用户信息失败, 未找到token");

    final int code;
    final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
