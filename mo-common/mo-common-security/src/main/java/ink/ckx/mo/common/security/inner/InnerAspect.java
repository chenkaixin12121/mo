package ink.ckx.mo.common.security.inner;

import cn.hutool.core.util.StrUtil;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.core.exception.BusinessException;
import ink.ckx.mo.common.core.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/12
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class InnerAspect {

    private final HttpServletRequest request;

    @SneakyThrows
    @Before("@within(inner) || @annotation(inner)")
    public void around(JoinPoint point, Inner inner) {
        // 实际注入的inner实体由表达式后一个注解决定，即是方法上的@Inner注解实体，若方法上无@Inner注解，则获取类上的
        if (inner == null) {
            Class<?> clazz = point.getTarget().getClass();
            inner = AnnotationUtils.findAnnotation(clazz, Inner.class);
        }
        String header = request.getHeader(CoreConstant.FROM);
        if (inner.value() && !StrUtil.equals(CoreConstant.FROM_IN, header)) {
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
    }
}