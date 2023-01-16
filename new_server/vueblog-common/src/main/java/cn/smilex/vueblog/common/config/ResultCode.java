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
    FORBIDDEN(403, "无权访问"),
    NOT_FOUND(404, "未找到"),
    METHOD_NOT_ALLOWED(405, "错误的请求方式"),
    UNKNOWN_ERROR(500, "未知错误"),
    ERROR_REQUEST_PARAM_ERROR(1000, "错误, 请求参数错误"),
    ERROR_LOGIN_NOT_FOUND_USER(2000, "错误, 登录失败未查询到该用户"),
    ERROR_LOGIN_PASSWORD_ERROR(2001, "错误, 登录失败密码错误"),
    ERROR_CURRENT_LOGIN_USER_INFO_ERROR_NOT_FOUND_TOKEN(2002, "错误, 获取当前登录用户信息失败, 未找到token"),
    ERROR_DUMP_ERROR_NOT_GET_DUMP_SQL(8000, "错误, 备份失败, 无法获取到备份SQL"),
    ERROR_SYSTEM_CONFIG_ERROR(10000, "错误, 系统配置错误!");

    final int code;
    final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
