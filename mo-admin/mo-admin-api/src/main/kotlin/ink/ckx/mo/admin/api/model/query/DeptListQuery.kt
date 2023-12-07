package ink.ckx.mo.admin.api.model.query

import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 部门列表Query
 *
 * @author chenkaixin
 */
@Schema(description = "部门列表Query")
data class DeptListQuery(

    @Schema(description = "关键字(部门名称)")
    var keywords: String? = null,

    @Schema(description = "状态")
    var status: StatusEnum? = null,
)