package ink.ckx.mo.admin.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import ink.ckx.mo.admin.api.constant.AdminConstant
import ink.ckx.mo.admin.api.enums.MenuTypeEnum
import ink.ckx.mo.admin.api.model.bo.RouteBO
import ink.ckx.mo.admin.api.model.entity.SysMenu
import ink.ckx.mo.admin.api.model.form.MenuForm
import ink.ckx.mo.admin.api.model.query.MenuListQuery
import ink.ckx.mo.admin.api.model.vo.menu.*
import ink.ckx.mo.admin.converter.MenuConverter
import ink.ckx.mo.admin.mapper.SysMenuMapper
import ink.ckx.mo.admin.service.SysMenuService
import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.web.enums.StatusEnum
import ink.ckx.mo.common.web.model.Option
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.*

/**
 * 菜单业务实现类
 *
 * @author chenkaixin
 */
@Service
class SysMenuServiceImpl(
    private val menuConverter: MenuConverter,
    private val redisTemplate: RedisTemplate<String, String>,
) : ServiceImpl<SysMenuMapper, SysMenu>(), SysMenuService {

    override fun listMenus(menuListQuery: MenuListQuery): List<MenuListVO> {
        val sysMenuList = ktQuery()
            .like(
                !menuListQuery.keywords.isNullOrBlank(),
                SysMenu::name,
                menuListQuery.keywords
            )
            .orderByDesc(SysMenu::sort)
            .list()
        val cacheMenuIds = sysMenuList.map(SysMenu::id).toMutableList()
        return sysMenuList.map {
            val parentId = it.parentId
            // parentId不在当前菜单ID的列表，说明为顶级菜单ID，根据此ID作为递归的开始条件节点
            if (parentId !in cacheMenuIds) {
                cacheMenuIds.add(parentId)
                return@map recurMenus(parentId, sysMenuList)
            }
            LinkedList<MenuListVO>()
        }.toList().flatten()
    }

    override fun saveMenu(menuForm: MenuForm): Long? {
        val sysMenu = convertSysMenu(menuForm)
        val result = save(sysMenu)
        if (result) {
            refreshRolePerm()
        }
        return sysMenu.id
    }

    override fun updateMenu(menuId: Long, menuForm: MenuForm) {
        val sysMenu = convertSysMenu(menuForm)
        sysMenu.id = menuId
        val result = updateById(sysMenu)
        if (result) {
            refreshRolePerm()
        }
    }

    private fun convertSysMenu(menuForm: MenuForm): SysMenu {
        val sysMenu = menuConverter.from2Entity(menuForm)
        // 如果是目录
        val path = menuForm.path
        if (menuForm.type === MenuTypeEnum.CATALOG) {
            if (path != null) {
                if (menuForm.parentId == AdminConstant.ROOT_ID && !path.startsWith("/")) {
                    // 一级目录需以 / 开头
                    sysMenu.path = "/$path"
                }
            }
            sysMenu.component = "Layout"
        }
        val treePath = generateMenuTreePath(menuForm.parentId)
        sysMenu.treePath = treePath
        return sysMenu
    }

    override fun deleteMenu(menuId: Long) {
        val result = ktUpdate()
            .eq(SysMenu::id, menuId)
            .or()
            .apply("CONCAT (',',tree_path,',') LIKE CONCAT('%,',{0},',%')", menuId)
            .remove()
        if (result) {
            refreshRolePerm()
        }
    }

    override fun listMenuOptions(): List<Option<Any>> {
        val menuList = ktQuery().orderByAsc(SysMenu::sort).list()
        return recurMenuOptions(AdminConstant.ROOT_ID, menuList)
    }

    override fun listRoutes(): List<RouteListVO> {
        val menuList = baseMapper.listRoutes()
        if (menuList.isEmpty()) {
            return ArrayList()
        }
        return recurRoutes(AdminConstant.ROOT_ID, menuList)
    }

    override fun getMenuDetail(menuId: Long): MenuDetailVO {
        val sysMenu = ktQuery()
            .select(
                SysMenu::id,
                SysMenu::parentId,
                SysMenu::type,
                SysMenu::name,
                SysMenu::path,
                SysMenu::component,
                SysMenu::perm,
                SysMenu::icon,
                SysMenu::sort,
                SysMenu::visible,
                SysMenu::alwaysShow
            )
            .eq(SysMenu::id, menuId)
            .one()
        return menuConverter.entity2DetailVO(sysMenu)
    }

    override fun listResources(): List<ResourceListVO> {
        val sysMenuList = ktQuery().orderByAsc(SysMenu::sort).list()
        return recurResources(AdminConstant.ROOT_ID, sysMenuList)
    }

    /**
     * 修改菜单显示状态
     *
     * @param menuId  菜单ID
     * @param visible 是否显示
     * @return
     */
    override fun updateMenuVisible(menuId: Long, visible: StatusEnum) {
        val result = ktUpdate().eq(SysMenu::id, menuId).set(SysMenu::visible, visible).update()
        if (result) {
            refreshRolePerm()
        }
    }

    override fun refreshRolePerm() {
        val deleteKeys = redisTemplate.keys(CoreConstant.ROLE_PERMS_CACHE_KEY_PREFIX + "*")
        if (deleteKeys.isNotEmpty()) {
            redisTemplate.delete(deleteKeys)
        }
        val rolePermBOList = baseMapper.listPermission()
        if (rolePermBOList.isNotEmpty()) {
            val rolePermMap =
                rolePermBOList.groupBy { e -> e.code }.mapValues { entry -> entry.value.map { e -> e.perm }.toSet() }
            for ((key, value) in rolePermMap) {
                redisTemplate.opsForSet().add(CoreConstant.ROLE_PERMS_CACHE_KEY_PREFIX + key, *value.toTypedArray())
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
    private fun recurMenus(parentId: Long?, menuList: List<SysMenu>): List<MenuListVO> {
        return if (menuList.isEmpty()) {
            mutableListOf()
        } else menuList
            .filter { it.parentId == parentId }
            .map {
                val menuListVO = menuConverter.entity2VO(it)
                val children = recurMenus(it.id, menuList)
                menuListVO.children = children
                menuListVO
            }
            .toList()
    }

    /**
     * 递归生成菜单下拉层级列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private fun recurMenuOptions(parentId: Long?, menuList: List<SysMenu>): List<Option<Any>> {
        return if (menuList.isEmpty()) {
            mutableListOf()
        } else menuList
            .filter { it.parentId == parentId }
            .map {
                Option(it.id, it.name, recurMenuOptions(it.id, menuList))
            }
            .toList()
    }

    /**
     * 递归生成资源树形列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private fun recurResources(parentId: Long?, menuList: List<SysMenu>): List<ResourceListVO> {
        return if (menuList.isEmpty()) {
            mutableListOf()
        } else menuList
            .filter { it.parentId == parentId }
            .map {
                val resourceListVO = ResourceListVO()
                resourceListVO.value = it.id
                resourceListVO.label = it.name
                val children = recurResources(it.id, menuList)
                resourceListVO.children = children
                resourceListVO
            }.toList()
    }

    /**
     * 递归生成菜单路由层级列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private fun recurRoutes(parentId: Long, menuList: List<RouteBO>): List<RouteListVO> {
        val routeListVOList = mutableListOf<RouteListVO>()
        for (routeBO in menuList) {
            if (routeBO.parentId == parentId) {
                val routeListVO = toRouteVO(routeBO)
                val children = routeBO.id?.let { recurRoutes(it, menuList) }
                if (!children.isNullOrEmpty()) {
                    routeListVO.children = children
                }
                routeListVOList.add(routeListVO)
            }
        }
        return routeListVOList
    }

    /**
     * 根据 RouteBO 创建 RouteListVO
     */
    private fun toRouteVO(routeBO: RouteBO): RouteListVO {
        val routeListVO = RouteListVO()
        if (MenuTypeEnum.MENU == routeBO.type) {
            // 根据name路由跳转 this.$router.push({name:xxx})
            routeListVO.name = routeBO.path
        }
        // 根据path路由跳转 this.$router.push({path:xxx})
        routeListVO.path = routeBO.path
        routeListVO.component = routeBO.component
        val meta = Meta()
        meta.title = routeBO.name
        meta.icon = routeBO.icon
        meta.roles = routeBO.roles
        meta.hidden = StatusEnum.DISABLE.value == routeBO.visible
        meta.keepAlive = true
        meta.alwaysShow = routeBO.alwaysShow
        routeListVO.meta = meta
        return routeListVO
    }

    /**
     * 部门路径生成
     *
     * @param parentId 父ID
     * @return
     */
    private fun generateMenuTreePath(parentId: Long?): String? {
        return if (AdminConstant.ROOT_ID == parentId) {
            parentId.toString()
        } else {
            val parent = ktQuery()
                .select(SysMenu::id, SysMenu::treePath)
                .eq(SysMenu::id, parentId)
                .one()
            if (parent != null) parent.treePath + "," + parent.id else null
        }
    }
}