package ink.ckx.mo.auth.authentication.mobile

import ink.ckx.mo.auth.authentication.base.BaseAuthenticationProvider
import ink.ckx.mo.auth.exception.MyAuthenticationException
import ink.ckx.mo.auth.userdetails.member.MemberUserDetailsService
import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.core.result.ResultCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.OAuth2Token
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/06
 */
class MobileGrantAuthenticationProvider(
    authorizationService: OAuth2AuthorizationService,
    tokenGenerator: OAuth2TokenGenerator<out OAuth2Token>,
    private val memberUserDetailsService: MemberUserDetailsService,
    private val redisTemplate: RedisTemplate<String, String>
) : BaseAuthenticationProvider<MobileGrantAuthenticationToken>(authorizationService, tokenGenerator) {

    @Value("\${spring.profiles.active}")
    private val env: String? = null

    override fun buildToken(reqParameters: Map<String, Any>): UsernamePasswordAuthenticationToken {
        // 手机号
        val mobile = reqParameters[CoreConstant.MOBILE] as String
        // 短信验证码
        val smsCode = reqParameters[CoreConstant.SMS_CODE] as String
        // 校验
        val flag = ("dev" == env && CoreConstant.SMS_CODE_VALUE == smsCode)
        if (!flag) {
            val cacheKey = CoreConstant.SMS_CODE_PREFIX + mobile
            val correctCode = redisTemplate.opsForValue()[cacheKey]
            // 验证码比对
            if (correctCode.isNullOrBlank() || smsCode != correctCode) {
                throw MyAuthenticationException(ResultCode.SMS_CODE_VERIFY_FAIL)
            }
            // 比对成功删除缓存的验证码
            redisTemplate.delete(cacheKey)
        }
        val userDetails = memberUserDetailsService.loadUserByMobile(mobile)
        return UsernamePasswordAuthenticationToken.authenticated(userDetails, null, userDetails.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return MobileGrantAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    override fun checkClient(registeredClient: RegisteredClient) {
        if (AuthorizationGrantType(CoreConstant.GRANT_TYPE_CUSTOM_MOBILE) !in registeredClient.authorizationGrantTypes) {
            throw MyAuthenticationException(ResultCode.UNSUPPORTED_GRANT_TYPE)
        }
    }
}