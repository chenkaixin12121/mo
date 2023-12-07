package ink.ckx.mo.auth.userdetails.member

import ink.ckx.mo.common.web.enums.StatusEnum
import ink.ckx.mo.member.api.model.dto.MemberUserInfoDTO
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/09
 */
data class MemberUserDetails(
    val userInfoDTO: MemberUserInfoDTO
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf()
    }

    override fun getPassword(): String? {
        return null
    }

    override fun getUsername(): String? {
        return userInfoDTO.mobile
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
        return StatusEnum.ENABLE == userInfoDTO.status
    }
}