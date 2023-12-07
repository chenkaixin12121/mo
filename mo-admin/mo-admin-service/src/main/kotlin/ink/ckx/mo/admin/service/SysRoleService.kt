package ink.ckx.mo.admin.service

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import ink.ckx.mo.admin.api.model.entity.SysRole
import ink.ckx.mo.admin.api.model.form.RoleForm
import ink.ckx.mo.admin.api.model.query.RolePageQuery
import ink.ckx.mo.admin.api.model.vo.role.RoleDetailVO
import ink.ckx.mo.admin.api.model.vo.role.RolePageVO
import ink.ckx.mo.common.web.enums.StatusEnum
import ink.ckx.mo.common.web.model.Option

/**
 * 角色业务接口层
 *
 * @author chenkaixin
 */
interface SysRoleService : IService<SysRole> {

    /**
     * 角色分页列表
     *
     * @param rolePageQuery
     * @return
     */
    fun listRolePages(rolePageQuery: RolePageQuery): Page<RolePageVO>

    /**
     * 角色下拉列表
     *
     * @return
     */
    fun listRoleOptions(): List<Option<Long>>

    /**
     * 获取角色详情
     *
     * @return
     */
    fun getRoleDetail(roleId: Long): RoleDetailVO?

    /**
     * 添加角色
     *
     * @param roleForm
     * @return
     */
    fun saveRole(roleForm: RoleForm): Long?

    /**
     * 更新角色
     *
     * @param roleId
     * @param roleForm
     * @return
     */
    fun updateRole(roleId: Long, roleForm: RoleForm)

    /**
     * 修改角色状态
     *
     * @param roleId
     * @param status
     * @return
     */
    fun updateRoleStatus(roleId: Long, status: StatusEnum)

    /**
     * 批量删除角色
     *
     * @param ids
     * @return
     */
    fun deleteRoles(ids: String)

    /**
     * 获取角色的资源ID集合,资源包括菜单和权限
     *
     * @param roleId
     * @return
     */
    fun getRoleMenuIds(roleId: Long): List<Long>

    /**
     * 修改角色的资源权限
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    fun updateRoleMenus(roleId: Long, menuIds: List<Long>)
}