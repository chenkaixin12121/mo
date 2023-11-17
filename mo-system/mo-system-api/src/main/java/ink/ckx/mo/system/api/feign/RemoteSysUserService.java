package ink.ckx.mo.system.api.feign;

import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.feign.config.FeignDecoderConfig;
import ink.ckx.mo.system.api.feign.fallback.RemoteSysUserServiceFallback;
import ink.ckx.mo.system.api.model.dto.SysUserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@FeignClient(value = "mo-system", contextId = "sys-user", fallback = RemoteSysUserServiceFallback.class, configuration = {FeignDecoderConfig.class})
public interface RemoteSysUserService {

    @GetMapping("/api/v1/users/{username}/authInfo")
    SysUserInfoDTO getUserInfo(@PathVariable String username, @RequestHeader(CoreConstant.FROM) String from);
}