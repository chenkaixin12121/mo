package ink.ckx.mo.admin.api.model.form

import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Schema(description = "字典数据项")
data class DictItemForm(

    @field:NotBlank(message = "类型编码不能为空")
    @Schema(description = "类型编码")
    var typeCode: String? = null,

    @field:NotBlank(message = "数据项名称不能为空")
    @Schema(description = "数据项名称")
    var name: String? = null,

    @field:NotBlank(message = "值不能为空")
    @Schema(description = "值")
    var value: String? = null,

    @field:NotNull(message = "状态不能为空")
    @Schema(description = "状态")
    var status: StatusEnum? = null,

    @field:NotNull(message = "排序不能为空")
    @Schema(description = "排序")
    var sort: Int? = null,
)