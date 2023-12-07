package ink.ckx.mo.admin.api.model.form

import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Schema(description = "部门表单对象")
data class DeptForm(

    @field:NotBlank(message = "部门名称不能为空")
    @Schema(description = "部门名称")
    var name: String? = null,

    @field:NotNull(message = "父部门ID不能为空")
    @Schema(description = "父部门ID")
    var parentId: Long? = null,

    @field:NotNull(message = "状态不能为空")
    @Schema(description = "状态")
    var status: StatusEnum? = null,

    @field:NotNull(message = "排序不能为空")
    @Schema(description = "排序")
    var sort: Int? = null,
)