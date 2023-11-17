package ink.ckx.mo.auth.authentication.mobile;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import ink.ckx.mo.auth.authentication.base.BaseAuthenticationConverter;
import ink.ckx.mo.auth.exception.MyAuthenticationException;
import ink.ckx.mo.auth.util.OAuth2EndpointUtils;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.core.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.Set;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/06
 */
public class MobileGrantAuthenticationConverter extends BaseAuthenticationConverter<MobileGrantAuthenticationToken> {

    @Override
    public boolean support(String grantType) {
        return CoreConstant.GRANT_TYPE_CUSTOM_MOBILE.equals(grantType);
    }

    @Override
    public void checkParams(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
        // 手机号
        String mobile = parameters.getFirst(CoreConstant.MOBILE);
        Assert.isFalse(StrUtil.isBlank(mobile), () -> new MyAuthenticationException(ResultCode.MOBILE_NOT_EMPTY));
        // 短信验证码
        String smsCode = parameters.getFirst(CoreConstant.SMS_CODE);
        Assert.isFalse(StrUtil.isBlank(smsCode), () -> new MyAuthenticationException(ResultCode.SMS_CODE_NOT_EMPTY));
    }

    @Override
    public MobileGrantAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        return new MobileGrantAuthenticationToken(new AuthorizationGrantType(CoreConstant.GRANT_TYPE_CUSTOM_MOBILE), clientPrincipal, requestedScopes, additionalParameters);
    }
}