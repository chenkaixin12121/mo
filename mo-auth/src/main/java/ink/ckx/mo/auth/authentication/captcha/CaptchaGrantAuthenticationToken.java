package ink.ckx.mo.auth.authentication.captcha;

import ink.ckx.mo.auth.authentication.base.BaseAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * @author chenkaixin
 * @description
 * @since 2023/12/04
 */
public class CaptchaGrantAuthenticationToken extends BaseAuthenticationToken {

    public CaptchaGrantAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
    }
}