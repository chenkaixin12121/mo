package ink.ckx.mo.auth.authentication.captcha;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import ink.ckx.mo.auth.authentication.base.BaseAuthenticationConverter;
import ink.ckx.mo.auth.exception.MyAuthenticationException;
import ink.ckx.mo.auth.util.OAuth2EndpointUtils;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.core.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.Set;

/**
 * @author chenkaixin
 * @description
 * @since 2023/12/04
 */
public class CaptchaGrantAuthenticationConverter extends BaseAuthenticationConverter<CaptchaGrantAuthenticationToken> {

    @Override
    public boolean support(String grantType) {
        return CoreConstant.GRANT_TYPE_CUSTOM_CAPTCHA.equals(grantType);
    }

    @Override
    public void checkParams(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
        String verifyCode = parameters.getFirst(CoreConstant.VERIFY_CODE);
        Assert.isFalse(CharSequenceUtil.isBlank(verifyCode), () -> new MyAuthenticationException(ResultCode.VERIFY_CODE_NOT_EMPTY));
        String verifyCodeKey = parameters.getFirst(CoreConstant.VERIFY_CODE_KEY);
        Assert.isFalse(CharSequenceUtil.isBlank(verifyCodeKey), () -> new MyAuthenticationException(ResultCode.VERIFY_CODE_KEY_NOT_EMPTY));
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        Assert.isFalse(CharSequenceUtil.isBlank(username), () -> new MyAuthenticationException(ResultCode.USERNAME_NOT_EMPTY));
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        Assert.isFalse(CharSequenceUtil.isBlank(password), () -> new MyAuthenticationException(ResultCode.PASSWORD_NOT_EMTPY));    }

    @Override
    public CaptchaGrantAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        return new CaptchaGrantAuthenticationToken(new AuthorizationGrantType(CoreConstant.GRANT_TYPE_CUSTOM_CAPTCHA), clientPrincipal, requestedScopes, additionalParameters);
    }
}