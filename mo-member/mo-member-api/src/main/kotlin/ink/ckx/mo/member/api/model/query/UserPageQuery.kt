package ink.ckx.mo.member.api.model.query

import ink.ckx.mo.common.core.base.BasePageQuery
import io.swagger.v3.oas.annotations.media.Schema

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/18
 */
@Schema(description = "会员用户分页Query")
data class UserPageQuery(

    @Schema(description = "关键字")
    var keywords: String? = null
) : BasePageQuery()