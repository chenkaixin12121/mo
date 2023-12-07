package ink.ckx.mo.auth.userdetails.member

import cn.hutool.core.lang.Assert
import ink.ckx.mo.auth.exception.MyAuthenticationException
import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.core.result.ResultCode
import ink.ckx.mo.member.api.feign.RemoteMemberUserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

/**
 * @author chenkaixin
 * @description 会员认证服务
 * @since 2023/11/09
 */
@Service
class MemberUserDetailsService(
    private val remoteMemberUserService: RemoteMemberUserService
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails? {
        return null
    }

    fun loadUserByMobile(mobile: String): UserDetails {
        val memberUserInfoDTO = remoteMemberUserService.getUserInfo(mobile, CoreConstant.FROM_IN)
        Assert.isFalse(memberUserInfoDTO == null) { MyAuthenticationException(ResultCode.USER_NOT_EXIST) }
        val memberUserDetails = MemberUserDetails(memberUserInfoDTO!!)
        Assert.isTrue(memberUserDetails.isEnabled) { MyAuthenticationException(ResultCode.USER_DISABLE) }
        return memberUserDetails
    }
}