package ink.ckx.mo.auth.userdetails.user;

import cn.hutool.core.collection.CollectionUtil;
import ink.ckx.mo.common.web.enums.StatusEnum;
import ink.ckx.mo.system.api.model.dto.SysUserInfoDTO;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/09
 */
@Data
public class SysUserDetails implements UserDetails {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 用户id
     */
    private Integer dataScope;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否禁用
     */
    private Boolean enabled;

    /**
     * 角色
     */
    private Collection<GrantedAuthority> authorities;

    public SysUserDetails(SysUserInfoDTO sysUserInfoDTO) {
        this.setUserId(sysUserInfoDTO.getUserId());
        this.setDeptId(sysUserInfoDTO.getDeptId());
        this.setDataScope(sysUserInfoDTO.getDataScope());
        this.setUsername(sysUserInfoDTO.getUsername());
        this.setPassword(sysUserInfoDTO.getPassword());
        this.setEnabled(StatusEnum.ENABLE.equals(sysUserInfoDTO.getStatus()));
        if (CollectionUtil.isNotEmpty(sysUserInfoDTO.getRoles())) {
            authorities = sysUserInfoDTO.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
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