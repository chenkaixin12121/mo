package ink.ckx.mo.auth.exception

import ink.ckx.mo.common.core.result.ResultCode
import org.springframework.security.core.AuthenticationException

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/06
 */
class MyAuthenticationException : AuthenticationException {

    val resultCode: ResultCode

    constructor(resultCode: ResultCode) : super(resultCode.msg) {
        this.resultCode = resultCode
    }

    constructor(resultCode: ResultCode, msg: String?) : super(msg) {
        this.resultCode = resultCode
    }
}