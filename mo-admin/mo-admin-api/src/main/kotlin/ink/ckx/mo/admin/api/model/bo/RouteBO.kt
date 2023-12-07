package ink.ckx.mo.admin.api.model.bo

import ink.ckx.mo.admin.api.enums.MenuTypeEnum

/**
 * 路由BO
 */
data class RouteBO(

    /**
     * 菜单id
     */
    var id: Long? = null,

    /**
     * 父菜单ID
     */
    var parentId: Long? = null,

    /**
     * 菜单名称
     */
    var name: String? = null,

    /**
     * 菜单类型(1-目录；2-菜单；3-按钮)
     */
    var type: MenuTypeEnum? = null,

    /**
     * 路由路径(浏览器地址栏路径)
     */
    var path: String? = null,

    /**
     * 组件路径(vue页面完整路径，省略.vue后缀)
     */
    var component: String? = null,

    /**
     * 权限标识
     */
    var perm: String? = null,

    /**
     * 显示状态
     */
    var visible: Int? = null,

    /**
     * 排序
     */
    var sort: Int? = null,

    /**
     * 菜单图标
     */
    var icon: String? = null,

    /**
     * 如果设置为 true，目录没有子节点也会显示
     */
    var alwaysShow: Boolean? = null,

    /**
     * 拥有路由的权限
     */
    var roles: List<String>? = null,
)