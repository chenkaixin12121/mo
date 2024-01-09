package ink.ckx.mo.admin.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import ink.ckx.mo.admin.api.model.entity.SysUserRole
import ink.ckx.mo.admin.mapper.SysUserRoleMapper
import ink.ckx.mo.admin.service.SysUserRoleService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SysUserRoleServiceImpl : ServiceImpl<SysUserRoleMapper, SysUserRole>(), SysUserRoleService {

    /**
     * 保存用户角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun saveUserRoles(userId: Long?, roleIds: List<Long?>) {
        // 用户原角色ID集合
        val userRoleIds = ktQuery()
            .eq(SysUserRole::userId, userId)
            .list()
            .map(SysUserRole::roleId)
            .toList()

        // 新增用户角色
        val saveRoleIds: List<Long?> = if (userRoleIds.isEmpty()) {
            roleIds
        } else {
            roleIds.filter { it !in userRoleIds }.toList()
        }
        val saveUserRoles = saveRoleIds.map { SysUserRole(null, userId, it) }.toList()
        this.saveBatch(saveUserRoles)

        // 删除用户角色
        if (userRoleIds.isNotEmpty()) {
            val removeRoleIds = userRoleIds.filter { it !in roleIds }.toList()
            if (removeRoleIds.isNotEmpty()) {
                ktUpdate()
                    .eq(SysUserRole::userId, userId)
                    .`in`(SysUserRole::roleId, removeRoleIds)
                    .remove()
            }
        }
    }
}