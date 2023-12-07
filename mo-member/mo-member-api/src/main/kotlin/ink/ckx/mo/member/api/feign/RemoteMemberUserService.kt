package ink.ckx.mo.member.api.feign

import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.feign.config.FeignDecoderConfig
import ink.ckx.mo.member.api.feign.fallback.RemoteMemberUserServiceFallback
import ink.ckx.mo.member.api.model.dto.MemberUserInfoDTO
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
    value = "mo-member",
    contextId = "member-user",
    fallback = RemoteMemberUserServiceFallback::class,
    configuration = [FeignDecoderConfig::class]
)
interface RemoteMemberUserService {

    @GetMapping("/appApi/v1/users/{mobile}/authInfo")
    fun getUserInfo(@PathVariable mobile: String, @RequestHeader(CoreConstant.FROM) from: String): MemberUserInfoDTO?
}