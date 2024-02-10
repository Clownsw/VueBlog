package vip.smilex.vueblog.common.exception;

import vip.smilex.vueblog.common.config.ResultCode;
import lombok.Getter;
import vip.smilex.vueblog.common.config.ResultCode;

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
