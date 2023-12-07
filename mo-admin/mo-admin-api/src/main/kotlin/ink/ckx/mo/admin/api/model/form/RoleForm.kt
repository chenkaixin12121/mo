package ink.ckx.mo.admin.api.model.form

import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Schema(description = "角色表单对象")
class RoleForm(

    @field:NotBlank(message = "角色名称不能为空")
    @Schema(description = "角色名称")
    val name: String? = null,

    @field:NotBlank(message = "角色编码不能为空")
    @Schema(description = "角色编码")
    val code: String? = null,

    @field:NotNull(message = "排序不能为空")
    @Schema(description = "排序")
    val sort: Int? = null,

    @field:NotNull(message = "数据权限不能为空")
    @Schema(description = "数据权限")
    val dataScope: Int? = null,

    @field:NotNull(message = "状态不能为空")
    @Schema(description = "状态")
    val status: StatusEnum? = null,
)