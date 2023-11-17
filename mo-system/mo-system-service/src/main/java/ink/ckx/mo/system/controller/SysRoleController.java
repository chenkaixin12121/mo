package ink.ckx.mo.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.ckx.mo.common.core.result.Result;
import ink.ckx.mo.common.mybatis.result.PageResult;
import ink.ckx.mo.common.web.model.Option;
import ink.ckx.mo.system.api.model.form.RoleForm;
import ink.ckx.mo.system.api.model.query.RolePageQuery;
import ink.ckx.mo.system.api.model.vo.role.RoleDetailVO;
import ink.ckx.mo.system.api.model.vo.role.RolePageVO;
import ink.ckx.mo.system.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "角色接口")
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    @Operation(summary = "角色分页列表")
    @PostMapping("/pages")
    public PageResult<RolePageVO> listRolePages(@RequestBody RolePageQuery rolePageQuery) {
        Page<RolePageVO> result = roleService.listRolePages(rolePageQuery);
        return PageResult.success(result);
    }

    @Operation(summary = "角色下拉列表")
    @GetMapping("/options")
    public Result<List<Option<Long>>> listRoleOptions() {
        List<Option<Long>> result = roleService.listRoleOptions();
        return Result.success(result);
    }

    @Operation(summary = "角色详情")
    @GetMapping("/{roleId}")
    public Result<RoleDetailVO> getRoleDetail(@Parameter(description = "角色ID") @PathVariable Long roleId) {
        RoleDetailVO roleDetailVO = roleService.getRoleDetail(roleId);
        return Result.success(roleDetailVO);
    }

    @PreAuthorize("@pms.hasPerm('sys:role:save')")
    @Operation(summary = "新增角色")
    @PostMapping
    public Result<Long> addRole(@Valid @RequestBody RoleForm roleForm) {
        Long roleId = roleService.saveRole(roleForm);
        return Result.success(roleId);
    }

    @PreAuthorize("@pms.hasPerm('sys:role:update')")
    @Operation(summary = "修改角色")
    @PutMapping(value = "/{roleId}")
    public Result<Void> updateRole(@Parameter(description = "角色ID") @PathVariable Long roleId,
                                   @Valid @RequestBody RoleForm roleForm) {
        roleService.updateRole(roleId, roleForm);
        return Result.success();
    }

    @PreAuthorize("@pms.hasPerm('sys:role:delete')")
    @Operation(summary = "删除角色")
    @DeleteMapping("/{ids}")
    public Result<Void> deleteRoles(@Parameter(description = "删除角色，多个以英文逗号(,)分割") @PathVariable String ids) {
        roleService.deleteRoles(ids);
        return Result.success();
    }

    @PreAuthorize("@pms.hasPerm('sys:role:update')")
    @Operation(summary = "修改角色状态")
    @PutMapping(value = "/{roleId}/status")
    public Result<Void> updateRoleStatus(@Parameter(description = "角色ID") @PathVariable Long roleId,
                                         @Parameter(description = "角色状态") @RequestParam Integer status) {
        roleService.updateRoleStatus(roleId, status);
        return Result.success();
    }

    @Operation(summary = "获取角色的菜单ID集合")
    @GetMapping("/{roleId}/menuIds")
    public Result<List<Long>> getRoleMenuIds(@Parameter(description = "角色ID") @PathVariable Long roleId) {
        List<Long> resourceIds = roleService.getRoleMenuIds(roleId);
        return Result.success(resourceIds);
    }

    @PreAuthorize("@pms.hasPerm('sys:role:resource')")
    @Operation(summary = "分配角色的菜单")
    @PutMapping("/{roleId}/menus")
    public Result<Void> updateRoleMenus(@Parameter(description = "角色ID") @PathVariable Long roleId,
                                        @Parameter(description = "菜单ID列表") @RequestBody List<Long> menuIds) {
        roleService.updateRoleMenus(roleId, menuIds);
        return Result.success();
    }
}