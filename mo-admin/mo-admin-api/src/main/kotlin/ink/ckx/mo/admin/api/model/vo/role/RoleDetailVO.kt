package ink.ckx.mo.admin.api.model.vo.role

import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/15
 */
@Schema(description = "角色详情VO")
data class RoleDetailVO(

    @Schema(description = "角色ID")
    var id: Long? = null,

    @Schema(description = "角色名称")
    var name: String? = null,

    @Schema(description = "角色编码")
    var code: String? = null,

    @Schema(description = "排序")
    var sort: Int? = null,

    @Schema(description = "数据权限")
    var dataScope: Int? = null,

    @Schema(description = "状态")
    var status: StatusEnum? = null,
)