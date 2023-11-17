package ink.ckx.mo.common.core.exception;

import ink.ckx.mo.common.core.result.ResultCode;
import lombok.Getter;

/**
 * @author chenkaixin
 * @description 自定义业务异常
 * @since 2023/11/10
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ResultCode resultCode;

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    public BusinessException(ResultCode resultCode, String msg) {
        super(msg);
        this.resultCode = resultCode;
    }
}