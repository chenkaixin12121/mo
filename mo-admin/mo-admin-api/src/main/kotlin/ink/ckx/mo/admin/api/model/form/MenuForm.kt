package ink.ckx.mo.admin.api.model.form

import ink.ckx.mo.admin.api.enums.MenuTypeEnum
import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/15
 */
@Schema(description = "菜单表单")
class MenuForm(

    @Schema(description = "父级id")
    var parentId: Long? = null,

    @field:NotNull(message = "菜单名称不能为空")
    @Schema(description = "名称")
    var name: String? = null,

    @field:NotNull(message = "菜单类型不能为空")
    @Schema(description = "菜单类型")
    var type: MenuTypeEnum? = null,

    @Schema(description = "图标")
    var icon: String? = null,

    @Schema(description = "始终显示")
    var alwaysShow: Int? = null,

    @Schema(description = "路由路径")
    var path: String? = null,

    @Schema(description = "权限标识")
    var perm: String? = null,

    @Schema(description = "组件路径")
    var component: String? = null,

    @field:NotNull(message = "排序不能为空")
    @Schema(description = "排序")
    var sort: Int? = null,

    @field:NotNull(message = "状态不能为空")
    @Schema(description = "状态")
    var visible: StatusEnum? = null,
)