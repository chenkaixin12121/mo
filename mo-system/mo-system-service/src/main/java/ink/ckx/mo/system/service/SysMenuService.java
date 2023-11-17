package ink.ckx.mo.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ink.ckx.mo.common.web.model.Option;
import ink.ckx.mo.system.api.model.entity.SysMenu;
import ink.ckx.mo.system.api.model.form.MenuForm;
import ink.ckx.mo.system.api.model.query.MenuListQuery;
import ink.ckx.mo.system.api.model.vo.menu.MenuDetailVO;
import ink.ckx.mo.system.api.model.vo.menu.MenuListVO;
import ink.ckx.mo.system.api.model.vo.menu.ResourceListVO;
import ink.ckx.mo.system.api.model.vo.menu.RouteListVO;

import java.util.List;

/**
 * 菜单业务接口
 *
 * @author chenkaixin
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获取菜单表格列表
     *
     * @return
     */
    List<MenuListVO> listMenus(MenuListQuery menuListQuery);

    /**
     * 获取菜单下拉列表
     *
     * @return
     */
    List<Option<Long>> listMenuOptions();

    /**
     * 新增菜单
     *
     * @param menuForm
     * @return
     */
    Long saveMenu(MenuForm menuForm);

    /**
     * 更新菜单
     *
     * @param menuId
     * @param menuForm
     * @return
     */
    void updateMenu(Long menuId, MenuForm menuForm);

    /**
     * 删除菜单
     *
     * @param menuId
     * @return
     */
    void deleteMenu(Long menuId);

    /**
     * 获取路由列表
     *
     * @return
     */
    List<RouteListVO> listRoutes();

    /**
     * 获取菜单详情
     *
     * @return
     */
    MenuDetailVO getMenuDetail(Long id);

    /**
     * 资源树形列表
     *
     * @return
     */
    List<ResourceListVO> listResources();

    /**
     * 修改菜单显示状态
     *
     * @param menuId  菜单ID
     * @param visible 是否显示
     * @return
     */
    void updateMenuVisible(Long menuId, Integer visible);

    /**
     * 刷新Redis缓存中角色权限
     *
     * @return
     */
    void refreshRolePerm();
}