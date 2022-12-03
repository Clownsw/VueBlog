package cn.smilex.vueblog.common.entity.common;

import cn.smilex.vueblog.common.config.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/11/11/8:11
 * @since 1.0
 */
@SuppressWarnings("unused")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static Result<?> success() {
        return fromResultCode(ResultCode.SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return fromResultCode(ResultCode.SUCCESS, data);
    }

    public static Result<?> error() {
        return fromResultCode(ResultCode.UNKNOWN_ERROR);
    }

    public static <T> Result<T> error(T data) {
        return fromResultCode(ResultCode.UNKNOWN_ERROR, data);
    }

    public static Result<?> fromResultCode(ResultCode resultCode) {
        return fromResultCode(resultCode, null);
    }

    public static <T> Result<T> fromResultCode(ResultCode resultCode, T data) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), data);
    }
}
