package ink.ckx.mo.auth.exception;

import ink.ckx.mo.common.core.result.ResultCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/06
 */
@Getter
public class MyAuthenticationException extends AuthenticationException {

    private final ResultCode resultCode;

    public MyAuthenticationException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    public MyAuthenticationException(ResultCode resultCode, String msg) {
        super(msg);
        this.resultCode = resultCode;
    }
}