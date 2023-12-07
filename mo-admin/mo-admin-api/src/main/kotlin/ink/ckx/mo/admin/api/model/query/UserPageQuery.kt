package ink.ckx.mo.admin.api.model.query

import ink.ckx.mo.common.core.base.BasePageQuery
import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 用户分页Query
 *
 * @author chenkaixin
 */
@Schema(description = "用户分页Query")
data class UserPageQuery(

    @Schema(description = "关键字(用户名/昵称/手机号)")
    var keywords: String? = null,

    @Schema(description = "用户状态")
    var status: StatusEnum? = null,

    @Schema(description = "部门ID")
    var deptId: Long? = null,
) : BasePageQuery()