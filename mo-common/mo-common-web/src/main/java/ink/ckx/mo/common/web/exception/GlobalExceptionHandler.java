package ink.ckx.mo.common.web.exception;

import cn.hutool.core.util.StrUtil;
import ink.ckx.mo.common.core.exception.BusinessException;
import ink.ckx.mo.common.core.result.Result;
import ink.ckx.mo.common.core.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * @author chenkaixin
 * @description 全局异常拦截
 * @since 2023/11/10
 */
@ConditionalOnExpression("!'${spring.application.name}'.equals('mo-auth')")
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public <T> Result<T> handlerHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return Result.fail(ResultCode.PARAM_ERROR, "解析参数失败");
    }

    @ExceptionHandler(BindException.class)
    public <T> Result<T> handlerBindException(BindException e) {
        String msg = e.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("；"));
        return Result.fail(ResultCode.PARAM_ERROR, msg);
    }

    @ExceptionHandler(BusinessException.class)
    public <T> Result<T> handlerBusinessException(BusinessException e) {
        ResultCode resultCode = e.getResultCode();
        return Result.fail(resultCode, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public <T> Result<T> handleException(Exception e) {
        String errorMsg = e.getMessage();
        if (StrUtil.contains(errorMsg, "Access Denied")) {
            return Result.fail(ResultCode.ACCESS_DENIED);
        }
        log.error("未知异常:", e);
        return Result.fail(errorMsg);
    }
}