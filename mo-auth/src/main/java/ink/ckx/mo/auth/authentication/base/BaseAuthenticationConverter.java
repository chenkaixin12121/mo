package ink.ckx.mo.auth.authentication.base;

import ink.ckx.mo.auth.exception.MyAuthenticationException;
import ink.ckx.mo.auth.util.OAuth2EndpointUtils;
import ink.ckx.mo.common.core.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chenkaixin
 * @description 身份验证转换器抽象类
 * @since 2023/11/11
 */
public abstract class BaseAuthenticationConverter<T extends BaseAuthenticationToken> implements AuthenticationConverter {

    /**
     * 是否支持此 convert
     *
     * @param grantType 授权类型
     * @return
     */
    public abstract boolean support(String grantType);

    /**
     * 校验参数
     *
     * @param request
     */
    public abstract void checkParams(HttpServletRequest request);

    /**
     * 构建具体类型的 token
     *
     * @param clientPrincipal
     * @param requestedScopes
     * @param additionalParameters
     * @return
     */
    public abstract T buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters);

    @Override
    public Authentication convert(HttpServletRequest request) {
        // grant_type (REQUIRED)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!this.support(grantType)) {
            return null;
        }
        // scope (OPTIONAL)
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            throw new MyAuthenticationException(ResultCode.INVALID_REQUEST, "OAuth 2.0 Parameter: " + OAuth2ParameterNames.SCOPE);
        }
        Set<String> requestedScopes = null;
        if (StringUtils.hasText(scope)) {
            requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }
        // 校验个性化参数
        this.checkParams(request);
        // 获取当前已经认证的客户端信息
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        // 扩展信息
        Map<String, Object> additionalParameters = parameters.entrySet().stream().filter(e -> !e.getKey().equals(OAuth2ParameterNames.GRANT_TYPE) && !e.getKey().equals(OAuth2ParameterNames.SCOPE)).collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));
        // 创建 token
        return this.buildToken(clientPrincipal, requestedScopes, additionalParameters);
    }
}