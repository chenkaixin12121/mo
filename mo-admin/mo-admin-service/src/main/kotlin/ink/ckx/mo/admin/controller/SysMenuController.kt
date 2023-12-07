package ink.ckx.mo.admin.controller

import ink.ckx.mo.admin.api.model.form.MenuForm
import ink.ckx.mo.admin.api.model.query.MenuListQuery
import ink.ckx.mo.admin.api.model.vo.menu.MenuDetailVO
import ink.ckx.mo.admin.api.model.vo.menu.MenuListVO
import ink.ckx.mo.admin.api.model.vo.menu.ResourceListVO
import ink.ckx.mo.admin.api.model.vo.menu.RouteListVO
import ink.ckx.mo.admin.service.SysMenuService
import ink.ckx.mo.common.core.result.Result
import ink.ckx.mo.common.core.result.Result.Companion.success
import ink.ckx.mo.common.web.enums.StatusEnum
import ink.ckx.mo.common.web.model.Option
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * 菜单控制器
 *
 * @author chenkaixin
 */
@Tag(name = "菜单接口")
@RestController
@RequestMapping("/api/v1/menus")
class SysMenuController(
    private val menuService: SysMenuService
) {

    @Operation(summary = "资源列表")
    @GetMapping("/resources")
    fun listResources(): Result<List<ResourceListVO>> {
        val resourceListVOList = menuService.listResources()
        return success(resourceListVOList)
    }

    @Operation(summary = "菜单列表")
    @PostMapping("/list")
    fun listMenus(@RequestBody menuListQuery: MenuListQuery): Result<List<MenuListVO>> {
        val menuListVOList = menuService.listMenus(menuListQuery)
        return success(menuListVOList)
    }

    @Operation(summary = "菜单下拉列表")
    @GetMapping("/options")
    fun listMenuOptions(): Result<List<Option<Any>>> {
        val result = menuService.listMenuOptions()
        return success(result)
    }

    @Operation(summary = "路由列表")
    @GetMapping("/routes")
    fun listRoutes(): Result<List<RouteListVO>> {
        val routeListVOList = menuService.listRoutes()
        return success(routeListVOList)
    }

    @Operation(summary = "菜单详情")
    @GetMapping("/{menuId}")
    fun detail(@Parameter(description = "菜单ID") @PathVariable menuId: Long): Result<MenuDetailVO?> {
        val menuDetailVO = menuService.getMenuDetail(menuId)
        return success(menuDetailVO)
    }

    @PreAuthorize("@pms.hasPerm('sys:menu:save')")
    @Operation(summary = "新增菜单")
    @PostMapping
    fun addMenu(@RequestBody @Valid menuForm: MenuForm): Result<Long?> {
        val menuId = menuService.saveMenu(menuForm)
        return success(menuId)
    }

    @PreAuthorize("@pms.hasPerm('sys:menu:update')")
    @Operation(summary = "修改菜单")
    @PutMapping(value = ["/{menuId}"])
    fun updateMenu(
        @Parameter(description = "菜单ID") @PathVariable menuId: Long,
        @RequestBody @Valid menuForm: MenuForm
    ): Result<Void?> {
        menuService.updateMenu(menuId, menuForm)
        return success()
    }

    @PreAuthorize("@pms.hasPerm('sys:menu:delete')")
    @Operation(summary = "删除菜单")
    @DeleteMapping("/{menuId}")
    fun deleteMenus(@Parameter(description = "菜单ID") @PathVariable menuId: Long): Result<Void?> {
        menuService.deleteMenu(menuId)
        return success()
    }

    @PreAuthorize("@pms.hasPerm('sys:menu:update:status')")
    @Operation(summary = "修改菜单显示状态")
    @PatchMapping("/{menuId}")
    fun updateMenuVisible(
        @Parameter(description = "菜单ID") @PathVariable menuId: Long,
        @Parameter(description = "显示状态") @RequestParam visible: StatusEnum
    ): Result<Void?> {
        menuService.updateMenuVisible(menuId, visible)
        return success()
    }
}