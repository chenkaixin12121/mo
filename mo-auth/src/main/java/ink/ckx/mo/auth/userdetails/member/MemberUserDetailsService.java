package ink.ckx.mo.auth.userdetails.member;

import cn.hutool.core.lang.Assert;
import ink.ckx.mo.auth.exception.MyAuthenticationException;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.core.result.ResultCode;
import ink.ckx.mo.member.api.feign.RemoteMemberUserService;
import ink.ckx.mo.member.api.model.dto.MemberUserInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author chenkaixin
 * @description 会员认证服务
 * @since 2023/11/09
 */
@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {

    private final RemoteMemberUserService remoteMemberUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByMobile(String mobile) {
        MemberUserInfoDTO memberUserInfoDTO = remoteMemberUserService.getUserInfo(mobile, CoreConstant.FROM_IN);
        Assert.isFalse(memberUserInfoDTO == null, () -> new MyAuthenticationException(ResultCode.USER_NOT_EXIST));
        MemberUserDetails memberUserDetails = new MemberUserDetails(memberUserInfoDTO);
        Assert.isTrue(memberUserDetails.isEnabled(), () -> new MyAuthenticationException(ResultCode.USER_DISABLE));
        return memberUserDetails;
    }
}