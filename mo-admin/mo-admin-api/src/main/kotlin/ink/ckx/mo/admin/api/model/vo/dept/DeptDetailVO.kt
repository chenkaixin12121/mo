package ink.ckx.mo.admin.api.model.vo.dept

import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "部门详情VO")
data class DeptDetailVO(

    @Schema(description = "部门ID")
    var id: Long,

    @Schema(description = "部门名称")
    var name: String,

    @Schema(description = "父部门ID")
    var parentId: Long,

    @Schema(description = "状态")
    var status: StatusEnum,

    @Schema(description = "排序")
    var sort: Int,
) 