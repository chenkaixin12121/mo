package ink.ckx.mo.admin.api.feign.fallback

import ink.ckx.mo.admin.api.feign.RemoteSysUserService
import ink.ckx.mo.admin.api.model.dto.SysUserInfoDTO
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@Component
class RemoteSysUserServiceFallback : RemoteSysUserService {

    private val log = KotlinLogging.logger {}

    override fun getUserInfo(username: String, from: String): SysUserInfoDTO {
        log.error { "feign 远程调用系统用户服务异常后的降级方法" }
        return SysUserInfoDTO()
    }
}