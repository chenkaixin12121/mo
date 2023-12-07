package ink.ckx.mo.admin.api.model.form

import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Schema(description = "字典类型")
data class DictTypeForm(

    @field:NotBlank(message = "类型名称不能为空")
    @Schema(description = "类型名称")
    var name: String? = null,

    @field:NotBlank(message = "类型编码不能为空")
    @Schema(description = "类型编码")
    var code: String? = null,

    @field:NotNull(message = "类型状态不能为空")
    @Schema(description = "类型状态")
    var status: StatusEnum? = null,

    @Schema(description = "备注")
    var remark: String? = null,
)