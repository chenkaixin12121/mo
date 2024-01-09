package ink.ckx.mo.admin.service.impl

import cn.hutool.core.lang.Assert
import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateChainWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import ink.ckx.mo.admin.api.model.entity.SysRole
import ink.ckx.mo.admin.api.model.entity.SysRoleMenu
import ink.ckx.mo.admin.api.model.entity.SysUserRole
import ink.ckx.mo.admin.api.model.form.RoleForm
import ink.ckx.mo.admin.api.model.query.RolePageQuery
import ink.ckx.mo.admin.api.model.vo.role.RoleDetailVO
import ink.ckx.mo.admin.api.model.vo.role.RolePageVO
import ink.ckx.mo.admin.converter.RoleConverter
import ink.ckx.mo.admin.mapper.SysRoleMapper
import ink.ckx.mo.admin.service.SysMenuService
import ink.ckx.mo.admin.service.SysRoleMenuService
import ink.ckx.mo.admin.service.SysRoleService
import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.core.exception.BusinessException
import ink.ckx.mo.common.core.result.ResultCode
import ink.ckx.mo.common.security.util.isSuperAdmin
import ink.ckx.mo.common.web.enums.StatusEnum
import ink.ckx.mo.common.web.model.Option
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/**
 * 角色业务实现类
 *
 * @author chenkaixin
 */
@Service
class SysRoleServiceImpl(
    private val sysRoleMenuService: SysRoleMenuService,
    private val roleConverter: RoleConverter,
    private val menuService: SysMenuService,
) : ServiceImpl<SysRoleMapper, SysRole>(), SysRoleService {


    /**
     * 角色分页列表
     *
     * @param rolePageQuery
     * @return
     */
    override fun listRolePages(rolePageQuery: RolePageQuery): Page<RolePageVO> {
        // 查询参数
        val pageNum = rolePageQuery.pageNum
        val pageSize = rolePageQuery.pageSize
        val keywords = rolePageQuery.keywords

        // 查询数据
        val rolePage = ktQuery()
            .like(!keywords.isNullOrBlank(), SysRole::name, keywords)
            // 非超级管理员不显示超级管理员角色
            .ne(!isSuperAdmin(), SysRole::code, CoreConstant.SUPER_ADMIN_CODE)
            .orderByDesc(SysRole::sort, SysRole::updateTime)
            .page(Page(pageNum, pageSize))

        // 实体转换
        return roleConverter.entity2Page(rolePage)
    }

    /**
     * 角色下拉列表
     *
     * @return
     */
    override fun listRoleOptions(): List<Option<Long>> {
        // 查询数据
        val roleList = ktQuery()
            .select(SysRole::id, SysRole::name)
            .ne(!isSuperAdmin(), SysRole::code, CoreConstant.SUPER_ADMIN_CODE)
            .orderByAsc(SysRole::sort)
            .list()

        // 实体转换
        return roleConverter.roles2Options(roleList)
    }

    override fun getRoleDetail(roleId: Long): RoleDetailVO {
        val entity = ktQuery()
            .select(
                SysRole::id,
                SysRole::dataScope,
                SysRole::name,
                SysRole::code,
                SysRole::sort,
                SysRole::status
            )
            .eq(SysRole::id, roleId)
            .one()

        return roleConverter.entity2DetailVO(entity)
    }

    /**
     * @param roleForm
     * @return
     */
    override fun saveRole(roleForm: RoleForm): Long? {
        val roleCode = roleForm.code
        val count = ktQuery()
            .eq(SysRole::code, roleCode)
            .count()

        Assert.isFalse(count > 0) { BusinessException(ResultCode.PARAM_ERROR, "角色编码重复，请检查！") }

        // 实体转换
        val sysRole = roleConverter.form2Entity(roleForm)
        val result = save(sysRole)
        if (result) {
            menuService.refreshRolePerm()
        }
        return sysRole.id
    }

    override fun updateRole(roleId: Long, roleForm: RoleForm) {
        val roleCode = roleForm.code
        val count = ktQuery()
            .ne(SysRole::id, roleId)
            .eq(SysRole::code, roleCode)
            .count()

        Assert.isFalse(count > 0) { BusinessException(ResultCode.PARAM_ERROR, "角色编码重复，请检查！") }

        // 实体转换
        val sysRole = roleConverter.form2Entity(roleForm)
        sysRole.id = roleId
        val result = updateById(sysRole)
        if (result) {
            menuService.refreshRolePerm()
        }
    }

    /**
     * 修改角色状态
     *
     * @param roleId
     * @param status
     * @return
     */
    override fun updateRoleStatus(roleId: Long, status: StatusEnum) {
        val result = ktUpdate()
            .eq(SysRole::id, roleId)
            .set(SysRole::status, status)
            .update()
        if (result) {
            menuService.refreshRolePerm()
        }
    }

    /**
     * 批量删除角色
     *
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deleteRoles(ids: String) {
        val roleIdList = ids.split(',').mapNotNull { it.toLongOrNull() }
        if (roleIdList.isEmpty()) {
            return
        }
        roleIdList.forEach {
            val count = KtQueryChainWrapper(SysUserRole()).eq(SysUserRole::roleId, it).count()
            Assert.isFalse(count > 0) { BusinessException(ResultCode.PARAM_ERROR, "该角色已分配用户，无法删除") }
            KtUpdateChainWrapper(SysRoleMenu()).eq(SysRoleMenu::roleId, it).remove()
        }
        val result = removeByIds(roleIdList)
        if (result) {
            menuService.refreshRolePerm()
        }
    }

    /**
     * 获取角色的资源ID集合,资源包括菜单和权限
     *
     * @param roleId
     * @return
     */
    override fun getRoleMenuIds(roleId: Long): List<Long> {
        // 获取角色拥有的菜单ID集合
        return sysRoleMenuService.listMenuIdsByRoleId(roleId)
    }

    /**
     * 修改角色的资源权限
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun updateRoleMenus(roleId: Long, menuIds: List<Long>) {
        // 删除角色菜单
        KtUpdateChainWrapper(SysRoleMenu()).eq(SysRoleMenu::id, roleId).remove()
        // 新增角色菜单
        if (menuIds.isNotEmpty()) {
            val roleMenus = menuIds.map { SysRoleMenu(null, roleId, it) }.toList()
            sysRoleMenuService.saveBatch(roleMenus)
        }
        menuService.refreshRolePerm()
    }
}