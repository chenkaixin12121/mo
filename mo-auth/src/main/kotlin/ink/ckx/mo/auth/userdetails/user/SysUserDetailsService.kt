package ink.ckx.mo.auth.userdetails.user

import cn.hutool.core.lang.Assert
import ink.ckx.mo.admin.api.feign.RemoteSysUserService
import ink.ckx.mo.auth.exception.MyAuthenticationException
import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.core.result.ResultCode
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

/**
 * @author chenkaixin
 * @description 系统用户认证
 * @since 2023/11/09
 */
@Service
class SysUserDetailsService(
    private val remoteSysUserService: RemoteSysUserService
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val sysUserInfo = remoteSysUserService.getUserInfo(username, CoreConstant.FROM_IN)
        Assert.isFalse(sysUserInfo == null) { MyAuthenticationException(ResultCode.USER_NOT_EXIST) }
        val sysUserDetails = SysUserDetails(sysUserInfo!!)
        Assert.isTrue(sysUserDetails.isEnabled) { MyAuthenticationException(ResultCode.USER_DISABLE) }
        return sysUserDetails
    }
}