package cn.smilex.vueblog.common.exception;

import cn.smilex.vueblog.common.config.ResultCode;
import lombok.Getter;

/**
 * @author smilex
 * @date 2022/12/3/23:41
 */
@Getter
public class VueBlogException extends RuntimeException {
    private final ResultCode resultCode;

    public VueBlogException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }
}
