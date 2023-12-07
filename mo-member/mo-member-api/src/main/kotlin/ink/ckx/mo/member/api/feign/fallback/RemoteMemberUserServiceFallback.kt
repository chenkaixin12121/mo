package ink.ckx.mo.member.api.feign.fallback

import ink.ckx.mo.member.api.feign.RemoteMemberUserService
import ink.ckx.mo.member.api.model.dto.MemberUserInfoDTO
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@Component
class RemoteMemberUserServiceFallback : RemoteMemberUserService {

    private val log = KotlinLogging.logger {}

    override fun getUserInfo(mobile: String, from: String): MemberUserInfoDTO {
        log.error { "feign 远程调用会员用户服务异常后的降级方法" }
        return MemberUserInfoDTO()
    }
}