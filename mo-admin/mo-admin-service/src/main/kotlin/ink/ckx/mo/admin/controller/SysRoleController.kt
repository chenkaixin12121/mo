package ink.ckx.mo.admin.controller

import ink.ckx.mo.admin.api.model.form.RoleForm
import ink.ckx.mo.admin.api.model.query.RolePageQuery
import ink.ckx.mo.admin.api.model.vo.role.RoleDetailVO
import ink.ckx.mo.admin.api.model.vo.role.RolePageVO
import ink.ckx.mo.admin.service.SysRoleService
import ink.ckx.mo.common.core.result.Result
import ink.ckx.mo.common.core.result.Result.Companion.success
import ink.ckx.mo.common.mybatis.result.PageResult
import ink.ckx.mo.common.web.enums.StatusEnum
import ink.ckx.mo.common.web.model.Option
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Tag(name = "角色接口")
@RestController
@RequestMapping("/api/v1/roles")
class SysRoleController(
    private val roleService: SysRoleService
) {

    @Operation(summary = "角色分页列表")
    @PostMapping("/pages")
    fun listRolePages(@RequestBody rolePageQuery: RolePageQuery): PageResult<RolePageVO> {
        val result = roleService.listRolePages(rolePageQuery)
        return PageResult.success(result)
    }

    @Operation(summary = "角色下拉列表")
    @GetMapping("/options")
    fun listRoleOptions(): Result<List<Option<Long>>> {
        val result = roleService.listRoleOptions()
        return success(result)
    }

    @Operation(summary = "角色详情")
    @GetMapping("/{roleId}")
    fun getRoleDetail(@Parameter(description = "角色ID") @PathVariable roleId: Long): Result<RoleDetailVO?> {
        val roleDetailVO = roleService.getRoleDetail(roleId)
        return success(roleDetailVO)
    }

    @PreAuthorize("@pms.hasPerm('sys:role:save')")
    @Operation(summary = "新增角色")
    @PostMapping
    fun addRole(@RequestBody @Valid roleForm: RoleForm): Result<Long?> {
        val roleId = roleService.saveRole(roleForm)
        return success(roleId)
    }

    @PreAuthorize("@pms.hasPerm('sys:role:update')")
    @Operation(summary = "修改角色")
    @PutMapping(value = ["/{roleId}"])
    fun updateRole(
        @Parameter(description = "角色ID") @PathVariable roleId: Long,
        @RequestBody @Valid roleForm: RoleForm
    ): Result<Void?> {
        roleService.updateRole(roleId, roleForm)
        return success()
    }

    @PreAuthorize("@pms.hasPerm('sys:role:delete')")
    @Operation(summary = "删除角色")
    @DeleteMapping("/{ids}")
    fun deleteRoles(@Parameter(description = "删除角色，多个以英文逗号(,)分割") @PathVariable ids: String): Result<Void?> {
        roleService.deleteRoles(ids)
        return success()
    }

    @PreAuthorize("@pms.hasPerm('sys:role:update')")
    @Operation(summary = "修改角色状态")
    @PutMapping(value = ["/{roleId}/status"])
    fun updateRoleStatus(
        @Parameter(description = "角色ID") @PathVariable roleId: Long,
        @Parameter(description = "角色状态") @RequestParam status: StatusEnum
    ): Result<Void?> {
        roleService.updateRoleStatus(roleId, status)
        return success()
    }

    @Operation(summary = "获取角色的菜单ID集合")
    @GetMapping("/{roleId}/menuIds")
    fun getRoleMenuIds(@Parameter(description = "角色ID") @PathVariable roleId: Long): Result<List<Long>> {
        val resourceIds = roleService.getRoleMenuIds(roleId)
        return success(resourceIds)
    }

    @PreAuthorize("@pms.hasPerm('sys:role:resource')")
    @Operation(summary = "分配角色的菜单")
    @PutMapping("/{roleId}/menus")
    fun updateRoleMenus(
        @Parameter(description = "角色ID") @PathVariable roleId: Long,
        @Parameter(description = "菜单ID列表") @RequestBody menuIds: List<Long>
    ): Result<Void?> {
        roleService.updateRoleMenus(roleId, menuIds)
        return success()
    }
}