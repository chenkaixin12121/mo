package ink.ckx.mo.admin.api.feign

import ink.ckx.mo.admin.api.feign.fallback.RemoteSysUserServiceFallback
import ink.ckx.mo.admin.api.model.dto.SysUserInfoDTO
import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.feign.config.FeignDecoderConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@FeignClient(
    value = "mo-admin",
    contextId = "sys-user",
    fallback = RemoteSysUserServiceFallback::class,
    configuration = [FeignDecoderConfig::class]
)
interface RemoteSysUserService {

    @GetMapping("/api/v1/users/{username}/authInfo")
    fun getUserInfo(@PathVariable username: String, @RequestHeader(CoreConstant.FROM) from: String): SysUserInfoDTO?
}