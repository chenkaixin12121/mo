package ink.ckx.mo.admin.api.model.vo.dict

import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "字典类型分页VO")
data class DictTypePageVO(

    @Schema(description = "字典类型ID")
    var id: Long? = null,

    @Schema(description = "类型名称")
    var name: String? = null,

    @Schema(description = "类型编码")
    var code: String? = null,

    @Schema(description = "类型状态")
    var status: StatusEnum? = null,
)