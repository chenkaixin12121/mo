package ink.ckx.mo.admin.api.model.vo.dict

import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "字典数据项分页VO")
data class DictItemPageVO(

    @Schema(description = "数据项ID")
    var id: Long? = null,

    @Schema(description = "数据项名称")
    var name: String? = null,

    @Schema(description = "值")
    var value: String? = null,

    @Schema(description = "类型状态")
    var status: StatusEnum? = null,
)