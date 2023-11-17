package ink.ckx.mo.auth.authentication.password;

import ink.ckx.mo.auth.authentication.base.BaseAuthenticationProvider;
import ink.ckx.mo.auth.exception.MyAuthenticationException;
import ink.ckx.mo.auth.userdetails.user.SysUserDetailsService;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.core.result.ResultCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Map;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/06
 */
public class PasswordGrantAuthenticationProvider extends BaseAuthenticationProvider<PasswordGrantAuthenticationToken> {

    private final SysUserDetailsService sysUserDetailsService;

    private final PasswordEncoder passwordEncoder;

    public PasswordGrantAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                               OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                               SysUserDetailsService sysUserDetailsService,
                                               PasswordEncoder passwordEncoder) {

        super(authorizationService, tokenGenerator);
        this.sysUserDetailsService = sysUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
        // 用户名密码校验
        String username = (String) reqParameters.get(OAuth2ParameterNames.USERNAME);
        String password = (String) reqParameters.get(OAuth2ParameterNames.PASSWORD);
        UserDetails userDetails = sysUserDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new MyAuthenticationException(ResultCode.PASSWORD_VERIFY_FAIL);
        }
        return UsernamePasswordAuthenticationToken.authenticated(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordGrantAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public void checkClient(RegisteredClient registeredClient) {
        if (!registeredClient.getAuthorizationGrantTypes().contains(new AuthorizationGrantType(CoreConstant.GRANT_TYPE_CUSTOM_PASSWORD))) {
            throw new MyAuthenticationException(ResultCode.UNSUPPORTED_GRANT_TYPE);
        }
    }
}