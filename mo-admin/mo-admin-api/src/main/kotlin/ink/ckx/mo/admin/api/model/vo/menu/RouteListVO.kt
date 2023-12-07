package ink.ckx.mo.admin.api.model.vo.menu

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 路由列表VO
 *
 * @author chenkaixin
 */
@Schema(description = "路由列表VO")
data class RouteListVO(

    @Schema(description = "路由路径")
    var path: String? = null,

    @Schema(description = "组件路径")
    var component: String? = null,

    @Schema(description = "路由路径")
    var name: String? = null,

    @Schema(description = "元数据")
    var meta: Meta? = null,

    @Schema(description = "子节点")
    var children: List<RouteListVO>? = null,
)

@Schema(description = "元数据")
data class Meta(

    @Schema(description = "标题")
    var title: String? = null,

    @Schema(description = "图标")
    var icon: String? = null,

    @Schema(description = "隐藏")
    var hidden: Boolean? = null,

    @Schema(description = "如果设置为 true，目录没有子节点也会显示")
    var alwaysShow: Boolean? = null,

    @Schema(description = "角色")
    var roles: List<String>? = null,

    @Schema(description = "页面缓存开启状态")
    var keepAlive: Boolean? = null,
)