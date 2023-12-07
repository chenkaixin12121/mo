package ink.ckx.mo.admin.service

import com.baomidou.mybatisplus.extension.service.IService
import ink.ckx.mo.admin.api.model.entity.SysMenu
import ink.ckx.mo.admin.api.model.form.MenuForm
import ink.ckx.mo.admin.api.model.query.MenuListQuery
import ink.ckx.mo.admin.api.model.vo.menu.MenuDetailVO
import ink.ckx.mo.admin.api.model.vo.menu.MenuListVO
import ink.ckx.mo.admin.api.model.vo.menu.ResourceListVO
import ink.ckx.mo.admin.api.model.vo.menu.RouteListVO
import ink.ckx.mo.common.web.enums.StatusEnum
import ink.ckx.mo.common.web.model.Option

/**
 * 菜单业务接口
 *
 * @author chenkaixin
 */
interface SysMenuService : IService<SysMenu> {

    /**
     * 获取菜单表格列表
     *
     * @return
     */
    fun listMenus(menuListQuery: MenuListQuery): List<MenuListVO>

    /**
     * 获取菜单下拉列表
     *
     * @return
     */
    fun listMenuOptions(): List<Option<Any>>

    /**
     * 新增菜单
     *
     * @param menuForm
     * @return
     */
    fun saveMenu(menuForm: MenuForm): Long?

    /**
     * 更新菜单
     *
     * @param menuId
     * @param menuForm
     * @return
     */
    fun updateMenu(menuId: Long, menuForm: MenuForm)

    /**
     * 删除菜单
     *
     * @param menuId
     * @return
     */
    fun deleteMenu(menuId: Long)

    /**
     * 获取路由列表
     *
     * @return
     */
    fun listRoutes(): List<RouteListVO>

    /**
     * 获取菜单详情
     *
     * @return
     */
    fun getMenuDetail(menuId: Long): MenuDetailVO

    /**
     * 资源树形列表
     *
     * @return
     */
    fun listResources(): List<ResourceListVO>

    /**
     * 修改菜单显示状态
     *
     * @param menuId  菜单ID
     * @param visible 是否显示
     * @return
     */
    fun updateMenuVisible(menuId: Long, visible: StatusEnum)

    /**
     * 刷新Redis缓存中角色权限
     *
     * @return
     */
    fun refreshRolePerm()
}