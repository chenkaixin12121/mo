package ink.ckx.mo.admin.service

import com.baomidou.mybatisplus.extension.service.IService
import ink.ckx.mo.admin.api.model.entity.SysRoleMenu

interface SysRoleMenuService : IService<SysRoleMenu> {

    /**
     * 获取角色拥有的菜单ID集合
     *
     * @param roleId
     * @return
     */
    fun listMenuIdsByRoleId(roleId: Long): List<Long>
}