package ink.ckx.mo.admin.api.model.vo.role

import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "角色分页VO")
data class RolePageVO(

    @Schema(description = "角色ID")
    var id: Long? = null,

    @Schema(description = "角色名称")
    var name: String? = null,

    @Schema(description = "角色编码")
    var code: String? = null,

    @Schema(description = "排序")
    var sort: Int? = null,

    @Schema(description = "更新时间")
    var updateTime: LocalDateTime? = null,

    @Schema(description = "创建时间")
    var createTime: LocalDateTime? = null,

    @Schema(description = "状态")
    var status: StatusEnum? = null
)