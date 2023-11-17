package ink.ckx.mo.member.api.feign.fallback;

import ink.ckx.mo.member.api.feign.RemoteMemberUserService;
import ink.ckx.mo.member.api.model.dto.MemberUserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@Slf4j
@Component
public class RemoteMemberUserServiceFallback implements RemoteMemberUserService {

    @Override
    public MemberUserInfoDTO getUserInfo(String username, String from) {
        log.error("feign 远程调用会员用户服务异常后的降级方法");
        return new MemberUserInfoDTO();
    }
}