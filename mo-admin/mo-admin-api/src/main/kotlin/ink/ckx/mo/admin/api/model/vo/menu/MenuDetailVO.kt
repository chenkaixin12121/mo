package ink.ckx.mo.admin.api.model.vo.menu

import ink.ckx.mo.admin.api.enums.MenuTypeEnum
import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "菜单详情VO")
data class MenuDetailVO(

    @Schema(description = "菜单id")
    var id: Long? = null,

    @Schema(description = "父级id")
    var parentId: Long? = null,

    @Schema(description = "名称")
    var name: String? = null,

    @Schema(description = "图标")
    var icon: String? = null,

    @Schema(description = "路由路径")
    var path: String? = null,

    @Schema(description = "始终显示")
    var alwaysShow: Int? = null,

    @Schema(description = "权限标识")
    var perm: String? = null,

    @Schema(description = "组件路径")
    var component: String? = null,

    @Schema(description = "排序")
    var sort: Int? = null,

    @Schema(description = "状态")
    var visible: StatusEnum? = null,

    @Schema(description = "类型")
    var type: MenuTypeEnum? = null,
) 