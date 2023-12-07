package ink.ckx.mo.admin.api.model.vo.menu

import ink.ckx.mo.admin.api.enums.MenuTypeEnum
import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "菜单列表VO")
data class MenuListVO(

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

    @Schema(description = "组件路径")
    var component: String? = null,

    @Schema(description = "排序")
    var sort: Int? = null,

    @Schema(description = "权限标识")
    var perm: String? = null,

    @Schema(description = "状态")
    var visible: StatusEnum? = null,

    @Schema(description = "菜单类型")
    var type: MenuTypeEnum? = null,

    @Schema(description = "更新时间")
    var updateTime: LocalDateTime? = null,

    @Schema(description = "创建时间")
    var createTime: LocalDateTime? = null,

    @Schema(description = "子节点")
    var children: List<MenuListVO>? = null,
) 