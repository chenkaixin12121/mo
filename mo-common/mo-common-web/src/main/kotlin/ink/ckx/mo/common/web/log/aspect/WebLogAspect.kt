package ink.ckx.mo.common.web.log.aspect

import cn.hutool.core.util.StrUtil
import cn.hutool.core.util.URLUtil
import cn.hutool.json.JSONUtil
import ink.ckx.mo.common.web.log.model.WebLog
import ink.ckx.mo.common.web.util.IpUtil
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.lang.reflect.Method

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/17
 */
@Aspect
@Component
class WebLogAspect {

    private val log = KotlinLogging.logger {}

    @Pointcut("execution(public * ink.ckx.mo.*.controller.*.*(..)) || execution(public * ink.ckx.mo.*.controller.*.*.*(..))")
    fun webLog() {
    }

    @Around("webLog()")
    fun doAround(joinPoint: ProceedingJoinPoint): Any {
        val startTime = System.currentTimeMillis()
        // 获取当前请求对象
        val attributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        val request = attributes.request
        // 记录请求信息
        val webLog = WebLog()
        val result = joinPoint.proceed()
        val signature = joinPoint.signature
        val methodSignature = signature as MethodSignature
        val method = methodSignature.method
        if (method.isAnnotationPresent(Operation::class.java)) {
            val log = method.getAnnotation(
                Operation::class.java
            )
            webLog.description = log.summary
        }
        val endTime = System.currentTimeMillis()
        val urlStr = request.requestURL.toString()
        webLog.basePath = StrUtil.removeSuffix(urlStr, URLUtil.url(urlStr).path)
        webLog.ip = IpUtil.getIpAddr(request)
        webLog.method = request.method
        webLog.parameter = getParameter(method, joinPoint.args)
        webLog.result = result
        webLog.spendTime = (endTime - startTime).toInt()
        webLog.startTime = startTime.toInt()
        webLog.uri = request.requestURI
        webLog.url = request.requestURL.toString()
        log.info { JSONUtil.toJsonStr(webLog) }
        return result
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private fun getParameter(method: Method, args: Array<Any>): Any? {
        val argList: MutableList<Any> = ArrayList()
        val parameters = method.parameters
        for (i in parameters.indices) {
            // 将 RequestBody 注解修饰的参数作为请求参数
            val requestBody = parameters[i].getAnnotation(
                RequestBody::class.java
            )
            if (requestBody != null) {
                argList.add(args[i])
            }
            // 将 RequestParam 注解修饰的参数作为请求参数
            val requestParam = parameters[i].getAnnotation(RequestParam::class.java)
            if (requestParam != null) {
                val map: MutableMap<String, Any> = HashMap()
                var key = parameters[i].name
                if (requestParam.value.isNotBlank()) {
                    key = requestParam.value
                }
                map[key] = args[i]
                argList.add(map)
            }
        }
        return when (argList.size) {
            0 -> {
                null
            }

            1 -> {
                argList[0]
            }

            else -> {
                argList
            }
        }
    }
}