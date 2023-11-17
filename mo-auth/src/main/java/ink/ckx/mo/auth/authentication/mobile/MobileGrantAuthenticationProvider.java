package ink.ckx.mo.auth.authentication.mobile;

import cn.hutool.core.util.StrUtil;
import ink.ckx.mo.auth.authentication.base.BaseAuthenticationProvider;
import ink.ckx.mo.auth.exception.MyAuthenticationException;
import ink.ckx.mo.auth.userdetails.member.MemberUserDetailsService;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.core.result.ResultCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Map;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/06
 */
public class MobileGrantAuthenticationProvider extends BaseAuthenticationProvider<MobileGrantAuthenticationToken> {

    private final MemberUserDetailsService memberUserDetailsService;

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${spring.profiles.active}")
    private String env;

    public MobileGrantAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                             OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                             MemberUserDetailsService memberUserDetailsService,
                                             RedisTemplate<String, String> redisTemplate) {
        super(authorizationService, tokenGenerator);
        this.memberUserDetailsService = memberUserDetailsService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
        // 手机号
        String mobile = (String) reqParameters.get(CoreConstant.MOBILE);
        // 短信验证码
        String smsCode = (String) reqParameters.get(CoreConstant.SMS_CODE);
        // 校验
        boolean flag = StrUtil.equals(env, "dev") && StrUtil.equals(CoreConstant.SMS_CODE_VALUE, smsCode);
        if (!flag) {
            String cacheKey = CoreConstant.SMS_CODE_PREFIX + mobile;
            String correctCode = redisTemplate.opsForValue().get(cacheKey);
            // 验证码比对
            if (StrUtil.isBlank(correctCode) || !StrUtil.equals(smsCode, correctCode)) {
                throw new MyAuthenticationException(ResultCode.SMS_CODE_VERIFY_FAIL);
            }
            // 比对成功删除缓存的验证码
            redisTemplate.delete(cacheKey);
        }
        UserDetails userDetails = memberUserDetailsService.loadUserByMobile(mobile);
        return UsernamePasswordAuthenticationToken.authenticated(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileGrantAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public void checkClient(RegisteredClient registeredClient) {
        if (!registeredClient.getAuthorizationGrantTypes().contains(new AuthorizationGrantType(CoreConstant.GRANT_TYPE_CUSTOM_MOBILE))) {
            throw new MyAuthenticationException(ResultCode.UNSUPPORTED_GRANT_TYPE);
        }
    }
}