package ink.ckx.mo.system.api.feign.fallback;

import ink.ckx.mo.system.api.feign.RemoteSysUserService;
import ink.ckx.mo.system.api.model.dto.SysUserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@Slf4j
@Component
public class RemoteSysUserServiceFallback implements RemoteSysUserService {

    @Override
    public SysUserInfoDTO getUserInfo(String username, String from) {
        log.error("feign 远程调用系统用户服务异常后的降级方法");
        return new SysUserInfoDTO();
    }
}