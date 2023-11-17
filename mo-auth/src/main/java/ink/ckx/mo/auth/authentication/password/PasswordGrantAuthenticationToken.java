package ink.ckx.mo.auth.authentication.password;

import ink.ckx.mo.auth.authentication.base.BaseAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/06
 */
public class PasswordGrantAuthenticationToken extends BaseAuthenticationToken {

    public PasswordGrantAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
    }
}