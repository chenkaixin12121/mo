package ink.ckx.mo.admin.api.model.vo.dept

import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "部门列表VO")
data class DeptListVO(

    @Schema(description = "部门id")
    var id: Long? = null,

    @Schema(description = "父部门id")
    var parentId: Long? = null,

    @Schema(description = "部门名称")
    var name: String? = null,

    @Schema(description = "排序")
    var sort: Int? = null,

    @Schema(description = "状态")
    var status: StatusEnum? = null,

    @Schema(description = "更新时间")
    var updateTime: LocalDateTime? = null,

    @Schema(description = "创建时间")
    var createTime: LocalDateTime? = null,

    @Schema(description = "子部门")
    var children: List<DeptListVO>? = null,
)