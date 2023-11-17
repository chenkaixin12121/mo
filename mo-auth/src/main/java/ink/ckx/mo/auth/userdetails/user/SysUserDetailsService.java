package ink.ckx.mo.auth.userdetails.user;

import cn.hutool.core.lang.Assert;
import ink.ckx.mo.auth.exception.MyAuthenticationException;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.core.result.ResultCode;
import ink.ckx.mo.system.api.feign.RemoteSysUserService;
import ink.ckx.mo.system.api.model.dto.SysUserInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author chenkaixin
 * @description 系统用户认证
 * @since 2023/11/09
 */
@RequiredArgsConstructor
@Service
public class SysUserDetailsService implements UserDetailsService {

    private final RemoteSysUserService remoteSysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserInfoDTO sysUserInfo = remoteSysUserService.getUserInfo(username, CoreConstant.FROM_IN);
        Assert.isFalse(sysUserInfo == null, () -> new MyAuthenticationException(ResultCode.USER_NOT_EXIST));
        SysUserDetails sysUserDetails = new SysUserDetails(sysUserInfo);
        Assert.isTrue(sysUserDetails.isEnabled(), () -> new MyAuthenticationException(ResultCode.USER_DISABLE));
        return sysUserDetails;
    }
}