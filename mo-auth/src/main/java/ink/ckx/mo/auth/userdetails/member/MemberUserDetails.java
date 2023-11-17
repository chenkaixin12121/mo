package ink.ckx.mo.auth.userdetails.member;

import ink.ckx.mo.common.web.enums.StatusEnum;
import ink.ckx.mo.member.api.model.dto.MemberUserInfoDTO;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/09
 */
@Data
public class MemberUserDetails implements UserDetails {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 手机号
     */
    private String username;

    /**
     * 是否禁用
     */
    private Boolean enabled;

    public MemberUserDetails(MemberUserInfoDTO memberUserInfoDTO) {
        this.setUserId(memberUserInfoDTO.getUserId());
        this.setUsername(memberUserInfoDTO.getMobile());
        this.setEnabled(StatusEnum.ENABLE.equals(memberUserInfoDTO.getStatus()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}