package ink.ckx.mo.auth.authentication.captcha;

import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.core.text.CharSequenceUtil;
import ink.ckx.mo.auth.authentication.base.BaseAuthenticationProvider;
import ink.ckx.mo.auth.exception.MyAuthenticationException;
import ink.ckx.mo.auth.userdetails.user.SysUserDetailsService;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.core.result.ResultCode;
import org.springframework.data.redis.core.RedisTemplate;
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
 * @since 2023/12/04
 */
public class CaptchaGrantAuthenticationProvider extends BaseAuthenticationProvider<CaptchaGrantAuthenticationToken> {

    private final SysUserDetailsService sysUserDetailsService;

    private final RedisTemplate<String, String> redisTemplate;

    private final PasswordEncoder passwordEncoder;

    public CaptchaGrantAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                              OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                              SysUserDetailsService sysUserDetailsService,
                                              RedisTemplate<String, String> redisTemplate,
                                              PasswordEncoder passwordEncoder) {

        super(authorizationService, tokenGenerator);
        this.sysUserDetailsService = sysUserDetailsService;
        this.redisTemplate = redisTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
        // 验证码校验
        String verifyCode = (String) reqParameters.get(CoreConstant.VERIFY_CODE);
        String verifyCodeKey = (String) reqParameters.get(CoreConstant.VERIFY_CODE_KEY);
        String cacheCode = redisTemplate.opsForValue().get(CoreConstant.VERIFY_CODE_CACHE_KEY_PREFIX + verifyCodeKey);
        MathGenerator mathGenerator = new MathGenerator();
        if (CharSequenceUtil.isBlank(cacheCode) || !mathGenerator.verify(cacheCode, verifyCode)) {
            throw new MyAuthenticationException(ResultCode.CODE_FAIL);
        }
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
        return CaptchaGrantAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public void checkClient(RegisteredClient registeredClient) {
        if (!registeredClient.getAuthorizationGrantTypes().contains(new AuthorizationGrantType(CoreConstant.GRANT_TYPE_CUSTOM_CAPTCHA))) {
            throw new MyAuthenticationException(ResultCode.UNSUPPORTED_GRANT_TYPE);
        }
    }
}