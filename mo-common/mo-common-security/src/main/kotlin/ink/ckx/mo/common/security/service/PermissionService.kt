package ink.ckx.mo.common.security.service

import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.security.util.getRoles
import ink.ckx.mo.common.security.util.isSuperAdmin
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.util.PatternMatchUtils

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/12
 */
@Service("pms")
class PermissionService(private val redisTemplate: RedisTemplate<String, String>) {

    /**
     * 判断当前登录用户是否拥有操作权限
     *
     * @param perm 权限标识
     * @return
     */
    fun hasPerm(perm: String?): Boolean {
        // 超级管理员放行
        if (isSuperAdmin()) {
            return true
        }
        // 校验权限
        if (perm.isNullOrBlank()) {
            return false
        }
        val roles = getRoles()
        if (roles.isEmpty()) {
            return false
        }
        val perms = redisTemplate.opsForSet()
            .union(roles.map { CoreConstant.ROLE_PERMS_CACHE_KEY_PREFIX + it }.toList())
        return perms?.any { PatternMatchUtils.simpleMatch(perm, it) } ?: false
    }
}