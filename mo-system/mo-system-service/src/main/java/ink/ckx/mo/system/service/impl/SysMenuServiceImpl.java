package ink.ckx.mo.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.web.enums.StatusEnum;
import ink.ckx.mo.common.web.model.Option;
import ink.ckx.mo.system.api.constant.SystemConstant;
import ink.ckx.mo.system.api.enums.MenuTypeEnum;
import ink.ckx.mo.system.api.model.bo.RouteBO;
import ink.ckx.mo.system.api.model.bo.RolePermBO;
import ink.ckx.mo.system.api.model.entity.SysMenu;
import ink.ckx.mo.system.api.model.form.MenuForm;
import ink.ckx.mo.system.api.model.query.MenuListQuery;
import ink.ckx.mo.system.api.model.vo.menu.MenuDetailVO;
import ink.ckx.mo.system.api.model.vo.menu.MenuListVO;
import ink.ckx.mo.system.api.model.vo.menu.ResourceListVO;
import ink.ckx.mo.system.api.model.vo.menu.RouteListVO;
import ink.ckx.mo.system.converter.MenuConverter;
import ink.ckx.mo.system.mapper.SysMenuMapper;
import ink.ckx.mo.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单业务实现类
 *
 * @author chenkaixin
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final MenuConverter menuConverter;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public List<MenuListVO> listMenus(MenuListQuery menuListQuery) {
        List<SysMenu> sysMenuList = this.list(new LambdaQueryWrapper<SysMenu>()
                .like(StrUtil.isNotBlank(menuListQuery.getKeywords()), SysMenu::getName, menuListQuery.getKeywords())
                .orderByAsc(SysMenu::getSort));

        Set<Long> cacheMenuIds = sysMenuList.stream().map(SysMenu::getId).collect(Collectors.toSet());

        return sysMenuList.stream().map(menu -> {
            Long parentId = menu.getParentId();
            // parentId不在当前菜单ID的列表，说明为顶级菜单ID，根据此ID作为递归的开始条件节点
            if (!cacheMenuIds.contains(parentId)) {
                cacheMenuIds.add(parentId);
                return this.recurMenus(parentId, sysMenuList);
            }
            return new LinkedList<MenuListVO>();
        }).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
    }

    @Override
    public Long saveMenu(MenuForm menuForm) {
        SysMenu sysMenu = this.convertSysMenu(menuForm);
        boolean result = this.save(sysMenu);
        if (result) {
            this.refreshRolePerm();
        }
        return sysMenu.getId();
    }

    @Override
    public void updateMenu(Long id, MenuForm menuForm) {
        SysMenu sysMenu = this.convertSysMenu(menuForm);
        sysMenu.setId(id);
        boolean result = this.updateById(sysMenu);
        if (result) {
            this.refreshRolePerm();
        }
    }

    private SysMenu convertSysMenu(MenuForm menuForm) {
        SysMenu sysMenu = menuConverter.from2Entity(menuForm);
        // 如果是目录
        String path = menuForm.getPath();
        if (menuForm.getType() == MenuTypeEnum.CATALOG) {
            if (Objects.equals(menuForm.getParentId(), SystemConstant.ROOT_ID) && !path.startsWith("/")) {
                // 一级目录需以 / 开头
                sysMenu.setPath("/" + path);
            }
            sysMenu.setComponent("Layout");
        }
        String treePath = this.generateMenuTreePath(menuForm.getParentId());
        sysMenu.setTreePath(treePath);
        return sysMenu;
    }

    @Override
    public void deleteMenu(Long menuId) {
        if (menuId != null) {
            boolean result = this.remove(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getId, menuId)
                    .or()
                    .apply("CONCAT (',',tree_path,',') LIKE CONCAT('%,',{0},',%')", menuId));
            if (result) {
                this.refreshRolePerm();
            }
        }
    }

    @Override
    public List<Option<Long>> listMenuOptions() {
        List<SysMenu> menuList = this.list(new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSort));
        return this.recurMenuOptions(SystemConstant.ROOT_ID, menuList);
    }

    @Override
    public List<RouteListVO> listRoutes() {
        List<RouteBO> menuList = this.baseMapper.listRoutes();
        return this.recurRoutes(SystemConstant.ROOT_ID, menuList);
    }

    @Override
    public MenuDetailVO getMenuDetail(Long menuId) {
        SysMenu sysMenu = this.getOne(new LambdaQueryWrapper<SysMenu>()
                .select(SysMenu::getId, SysMenu::getParentId, SysMenu::getType, SysMenu::getName, SysMenu::getPath,
                        SysMenu::getComponent, SysMenu::getPerm, SysMenu::getIcon, SysMenu::getSort, SysMenu::getVisible)
                .eq(SysMenu::getId, menuId));

        return menuConverter.entity2DetailVO(sysMenu);
    }

    @Override
    public List<ResourceListVO> listResources() {
        List<SysMenu> sysMenuList = this.list(new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSort));
        return this.recurResources(SystemConstant.ROOT_ID, sysMenuList);
    }

    /**
     * 修改菜单显示状态
     *
     * @param menuId  菜单ID
     * @param visible 是否显示
     * @return
     */
    @Override
    public void updateMenuVisible(Long menuId, Integer visible) {
        boolean result = this.update(new LambdaUpdateWrapper<SysMenu>()
                .eq(SysMenu::getId, menuId)
                .set(SysMenu::getVisible, visible));
        if (result) {
            this.refreshRolePerm();
        }
    }

    @Override
    public void refreshRolePerm() {
        Set<String> deleteKeys = redisTemplate.keys(CoreConstant.ROLE_PERMS_CACHE_KEY_PREFIX + "*");
        if (CollUtil.isNotEmpty(deleteKeys)) {
            redisTemplate.delete(deleteKeys);
        }
        List<RolePermBO> rolePermBOList = this.baseMapper.listPermission();
        if (CollUtil.isNotEmpty(rolePermBOList)) {
            Map<String, Set<String>> rolePermMap = rolePermBOList.stream()
                    .collect(Collectors.groupingBy(RolePermBO::getCode, Collectors.mapping(RolePermBO::getPerm, Collectors.toSet())));
            for (Map.Entry<String, Set<String>> entry : rolePermMap.entrySet()) {
                redisTemplate.opsForSet().add(CoreConstant.ROLE_PERMS_CACHE_KEY_PREFIX + entry.getKey(), entry.getValue().toArray(new String[0]));
            }
        }
    }

    /**
     * 递归生成菜单列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private List<MenuListVO> recurMenus(Long parentId, List<SysMenu> menuList) {
        if (CollUtil.isEmpty(menuList)) {
            return new ArrayList<>();
        }

        return menuList.stream().filter(menu -> menu.getParentId().equals(parentId)).map(entity -> {
            MenuListVO menuListVO = menuConverter.entity2VO(entity);
            List<MenuListVO> children = recurMenus(entity.getId(), menuList);
            menuListVO.setChildren(children);
            return menuListVO;
        }).collect(Collectors.toList());
    }

    /**
     * 递归生成菜单下拉层级列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private List<Option<Long>> recurMenuOptions(Long parentId, List<SysMenu> menuList) {
        if (CollUtil.isEmpty(menuList)) {
            return new ArrayList<>();
        }

        return menuList.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> new Option<>(menu.getId(), menu.getName(), recurMenuOptions(menu.getId(), menuList)))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * 递归生成资源树形列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private List<ResourceListVO> recurResources(Long parentId, List<SysMenu> menuList) {
        if (CollUtil.isEmpty(menuList)) {
            return new ArrayList<>();
        }

        return menuList.stream().filter(menu -> menu.getParentId().equals(parentId)).map(menu -> {
            ResourceListVO resourceListVO = new ResourceListVO();
            resourceListVO.setValue(menu.getId());
            resourceListVO.setLabel(menu.getName());

            List<ResourceListVO> children = recurResources(menu.getId(), menuList);
            resourceListVO.setChildren(children);

            return resourceListVO;
        }).collect(Collectors.toList());
    }

    /**
     * 递归生成菜单路由层级列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private List<RouteListVO> recurRoutes(Long parentId, List<RouteBO> menuList) {
        List<RouteListVO> routeListVOList = new ArrayList<>();
        for (RouteBO routeBO : menuList) {
            if (routeBO.getParentId().equals(parentId)) {
                RouteListVO routeListVO = this.toRouteVO(routeBO);
                List<RouteListVO> children = this.recurRoutes(routeBO.getId(), menuList);
                if (CollUtil.isNotEmpty(children)) {
                    routeListVO.setChildren(children);
                }
                routeListVOList.add(routeListVO);
            }
        }
        return routeListVOList;
    }

    /**
     * 根据 RouteBO 创建 RouteListVO
     */
    private RouteListVO toRouteVO(RouteBO routeBO) {
        RouteListVO routeListVO = new RouteListVO();

        if (MenuTypeEnum.MENU.equals(routeBO.getType())) {
            //  根据name路由跳转 this.$router.push({name:xxx})
            routeListVO.setName(routeBO.getPath());
        }
        // 根据path路由跳转 this.$router.push({path:xxx})
        routeListVO.setPath(routeBO.getPath());
        routeListVO.setComponent(routeBO.getComponent());

        RouteListVO.Meta meta = new RouteListVO.Meta();
        meta.setTitle(routeBO.getName());
        meta.setIcon(routeBO.getIcon());
        meta.setRoles(routeBO.getRoles());
        meta.setHidden(StatusEnum.DISABLE.getValue().equals(routeBO.getVisible()));
        meta.setKeepAlive(true);
        meta.setAlwaysShow(routeBO.getAlwaysShow());
        routeListVO.setMeta(meta);

        return routeListVO;
    }

    /**
     * 部门路径生成
     *
     * @param parentId 父ID
     * @return
     */
    private String generateMenuTreePath(Long parentId) {
        if (SystemConstant.ROOT_ID.equals(parentId)) {
            return String.valueOf(parentId);
        } else {
            SysMenu parent = this.getOne(new LambdaQueryWrapper<SysMenu>()
                    .select(SysMenu::getId, SysMenu::getTreePath)
                    .eq(SysMenu::getId, parentId));
            return parent != null ? parent.getTreePath() + "," + parent.getId() : null;
        }
    }
}