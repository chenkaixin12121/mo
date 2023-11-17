package ink.ckx.mo.system.controller;

import ink.ckx.mo.common.core.result.Result;
import ink.ckx.mo.common.web.model.Option;
import ink.ckx.mo.system.api.model.form.MenuForm;
import ink.ckx.mo.system.api.model.query.MenuListQuery;
import ink.ckx.mo.system.api.model.vo.menu.MenuDetailVO;
import ink.ckx.mo.system.api.model.vo.menu.MenuListVO;
import ink.ckx.mo.system.api.model.vo.menu.ResourceListVO;
import ink.ckx.mo.system.api.model.vo.menu.RouteListVO;
import ink.ckx.mo.system.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单控制器
 *
 * @author chenkaixin
 */
@Tag(name = "菜单接口")
@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService menuService;

    @Operation(summary = "资源列表")
    @GetMapping("/resources")
    public Result<List<ResourceListVO>> listResources() {
        List<ResourceListVO> resourceListVOList = menuService.listResources();
        return Result.success(resourceListVOList);
    }

    @Operation(summary = "菜单列表")
    @PostMapping("/list")
    public Result<List<MenuListVO>> listMenus(@RequestBody MenuListQuery menuListQuery) {
        List<MenuListVO> menuListVOList = menuService.listMenus(menuListQuery);
        return Result.success(menuListVOList);
    }

    @Operation(summary = "菜单下拉列表")
    @GetMapping("/options")
    public Result<List<Option<Long>>> listMenuOptions() {
        List<Option<Long>> result = menuService.listMenuOptions();
        return Result.success(result);
    }

    @Operation(summary = "路由列表")
    @GetMapping("/routes")
    public Result<List<RouteListVO>> listRoutes() {
        List<RouteListVO> routeListVOList = menuService.listRoutes();
        return Result.success(routeListVOList);
    }

    @Operation(summary = "菜单详情")
    @GetMapping("/{menuId}")
    public Result<MenuDetailVO> detail(@Parameter(description = "菜单ID") @PathVariable Long menuId) {
        MenuDetailVO menuDetailVO = menuService.getMenuDetail(menuId);
        return Result.success(menuDetailVO);
    }

    @PreAuthorize("@pms.hasPerm('sys:menu:save')")
    @Operation(summary = "新增菜单")
    @PostMapping
    public Result<Long> addMenu(@Valid @RequestBody MenuForm menuForm) {
        Long menuId = menuService.saveMenu(menuForm);
        return Result.success(menuId);
    }

    @PreAuthorize("@pms.hasPerm('sys:menu:update')")
    @Operation(summary = "修改菜单")
    @PutMapping(value = "/{menuId}")
    public Result<Void> updateMenu(@Parameter(description = "菜单ID") @PathVariable Long menuId,
                                   @Valid @RequestBody MenuForm menuForm) {
        menuService.updateMenu(menuId, menuForm);
        return Result.success();
    }

    @PreAuthorize("@pms.hasPerm('sys:menu:delete')")
    @Operation(summary = "删除菜单")
    @DeleteMapping("/{menuId}")
    public Result<Void> deleteMenus(@Parameter(description = "菜单ID") @PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return Result.success();
    }

    @PreAuthorize("@pms.hasPerm('sys:menu:update:status')")
    @Operation(summary = "修改菜单显示状态")
    @PatchMapping("/{menuId}")
    public Result<Void> updateMenuVisible(@Parameter(description = "菜单ID") @PathVariable Long menuId,
                                          @Parameter(description = "显示状态") @RequestParam Integer visible) {
        menuService.updateMenuVisible(menuId, visible);
        return Result.success();
    }
}