package ink.ckx.mo.common.web.exception

import cn.hutool.core.util.StrUtil
import ink.ckx.mo.common.core.exception.BusinessException
import ink.ckx.mo.common.core.result.Result
import ink.ckx.mo.common.core.result.Result.Companion.fail
import ink.ckx.mo.common.core.result.ResultCode
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.ConstraintViolationException
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


/**
 * @author chenkaixin
 * @description 全局异常拦截
 * @since 2023/11/10
 */
@ConditionalOnExpression("!'\${spring.application.name}'.equals('mo-auth')")
@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = KotlinLogging.logger {}

    @ExceptionHandler(ConstraintViolationException::class)
    fun <T> handlerConstraintViolationException(e: ConstraintViolationException): Result<T> {
        val msg = e.constraintViolations.joinToString("；") { it.message }
        return fail(ResultCode.PARAM_ERROR, msg)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun <T> handlerHttpMessageNotReadableException(e: HttpMessageNotReadableException): Result<T> {
        return fail(ResultCode.PARAM_ERROR, "解析参数失败")
    }

    @ExceptionHandler(BindException::class)
    fun <T> handlerBindException(e: BindException): Result<T> {
        val msg = e.allErrors.joinToString(separator = "；") { obj -> obj?.defaultMessage ?: "" }
        return fail(ResultCode.PARAM_ERROR, msg)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun <T> handlerMethodArgumentNotValidException(e: MethodArgumentNotValidException): Result<T> {
        val msg = e.bindingResult.allErrors.joinToString(separator = "；") { it.defaultMessage ?: "" }
        return fail(ResultCode.PARAM_ERROR, msg)
    }

    @ExceptionHandler(BusinessException::class)
    fun <T> handlerBusinessException(e: BusinessException): Result<T> {
        val resultCode: ResultCode = e.resultCode
        return fail(resultCode, e.message)
    }

    @ExceptionHandler(Exception::class)
    fun <T> handleException(e: Exception): Result<T> {
        if (StrUtil.contains(e.message, "Access Denied")) {
            return fail(ResultCode.ACCESS_DENIED)
        }
        log.error(e) { "未知异常:" }
        // 兜底异常不向客户端暴露内部错误信息，统一返回失败
        return fail(ResultCode.FAIL)
    }
}