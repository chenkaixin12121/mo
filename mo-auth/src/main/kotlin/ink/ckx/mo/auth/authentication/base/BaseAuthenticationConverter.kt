package ink.ckx.mo.auth.authentication.base

import ink.ckx.mo.auth.exception.MyAuthenticationException
import ink.ckx.mo.auth.util.OAuth2EndpointUtils
import ink.ckx.mo.common.core.result.ResultCode
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.web.authentication.AuthenticationConverter

/**
 * @author chenkaixin
 * @description 身份验证转换器抽象类
 * @since 2023/11/11
 */
abstract class BaseAuthenticationConverter<T : BaseAuthenticationToken> : AuthenticationConverter {

    /**
     * 是否支持此 convert
     *
     * @param grantType 授权类型
     * @return
     */
    abstract fun support(grantType: String): Boolean

    /**
     * 校验参数
     *
     * @param request
     */
    abstract fun checkParams(request: HttpServletRequest)

    /**
     * 构建具体类型的 token
     *
     * @param clientPrincipal
     * @param requestedScopes
     * @param additionalParameters
     * @return
     */
    abstract fun buildToken(
        clientPrincipal: Authentication,
        requestedScopes: Set<String>,
        additionalParameters: Map<String, Any>
    ): T

    override fun convert(request: HttpServletRequest): Authentication? {
        // grant_type (REQUIRED)
        val grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE)
        if (!support(grantType)) {
            return null
        }
        // scope (OPTIONAL)
        val parameters = OAuth2EndpointUtils.getParameters(request)
        val scope = parameters.getFirst(OAuth2ParameterNames.SCOPE)
        if (!scope.isNullOrBlank() && parameters[OAuth2ParameterNames.SCOPE]?.size != 1) {
            throw MyAuthenticationException(
                ResultCode.INVALID_REQUEST,
                "OAuth 2.0 Parameter: " + OAuth2ParameterNames.SCOPE
            )
        }
        val requestedScopes = if (!scope.isNullOrBlank()) {
            scope.split(" ").toSet()
        } else {
            setOf()
        }
        // 校验个性化参数
        checkParams(request)
        // 获取当前已经认证的客户端信息
        val clientPrincipal = SecurityContextHolder.getContext().authentication
        // 扩展信息
        val additionalParameters: Map<String, Any> = parameters.entries
            .filter { it.key != OAuth2ParameterNames.GRANT_TYPE && it.key != OAuth2ParameterNames.SCOPE }
            .associate { it.key to it.value[0] }
        // 创建 token
        return buildToken(clientPrincipal, requestedScopes, additionalParameters)
    }
}

fun checkNotBlank(value: String?, errorCode: ResultCode) {
    require(!value.isNullOrBlank()) { throw MyAuthenticationException(errorCode) }
}