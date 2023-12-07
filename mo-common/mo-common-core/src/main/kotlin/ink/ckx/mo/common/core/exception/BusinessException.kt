package ink.ckx.mo.common.core.exception

import ink.ckx.mo.common.core.result.ResultCode

/**
 * @author chenkaixin
 * @description 自定义业务异常
 * @since 2023/11/10
 */
class BusinessException : RuntimeException {

    val resultCode: ResultCode

    constructor(resultCode: ResultCode) : super(resultCode.msg) {
        this.resultCode = resultCode
    }

    constructor(resultCode: ResultCode, msg: String) : super(msg) {
        this.resultCode = resultCode
    }
}