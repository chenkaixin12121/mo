package ink.ckx.mo.admin.service

import com.baomidou.mybatisplus.extension.service.IService
import ink.ckx.mo.admin.api.model.entity.SysUserRole

interface SysUserRoleService : IService<SysUserRole> {

    /**
     * 保存用户角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    fun saveUserRoles(userId: Long?, roleIds: List<Long?>)

    /**
     * 根据用户ID批量删除用户角色关联
     *
     * @param userIds 用户ID集合
     */
    fun deleteUserRoles(userIds: Collection<Long>)
}