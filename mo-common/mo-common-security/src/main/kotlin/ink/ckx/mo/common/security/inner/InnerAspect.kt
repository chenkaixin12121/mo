package ink.ckx.mo.common.security.inner

import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.core.exception.BusinessException
import ink.ckx.mo.common.core.result.ResultCode
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.core.annotation.AnnotationUtils

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/12
 */
@Aspect
class InnerAspect(private val request: HttpServletRequest) {

    @Before("@within(inner) || @annotation(inner)")
    fun around(point: JoinPoint, inner: Inner?) {
        // 实际注入的inner实体由表达式后一个注解决定，即是方法上的@Inner注解实体，若方法上无@Inner注解，则获取类上的
        var tempInner = inner;
        if (tempInner == null) {
            val clazz: Class<*> = point.target.javaClass
            tempInner = AnnotationUtils.findAnnotation(clazz, Inner::class.java)
        }
        val header = request.getHeader(CoreConstant.FROM)
        if (tempInner!!.value && CoreConstant.FROM_IN != header) {
            throw BusinessException(ResultCode.ACCESS_DENIED)
        }
    }
}