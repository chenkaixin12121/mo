package ink.ckx.mo.auth.userdetails.user

import ink.ckx.mo.admin.api.model.dto.SysUserInfoDTO
import ink.ckx.mo.common.web.enums.StatusEnum
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/09
 */
data class SysUserDetails(
    val userInfoDTO: SysUserInfoDTO
) : UserDetails {

    override fun getAuthorities(): Set<GrantedAuthority>? {
        return userInfoDTO.roles
            ?.map { role -> SimpleGrantedAuthority(role) }
            ?.toSet()
    }

    override fun getPassword(): String? {
        return userInfoDTO.password
    }

    override fun getUsername(): String? {
        return userInfoDTO.username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return userInfoDTO.status == StatusEnum.ENABLE
    }
}