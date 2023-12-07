package ink.ckx.mo.admin.api.model.query

import ink.ckx.mo.common.core.base.BasePageQuery
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 角色分页Query
 *
 * @author chenkaixin
 */
@Schema(description = "角色分页Query")
data class RolePageQuery(

    @Schema(description = "关键字(角色名称)")
    var keywords: String? = null,
) : BasePageQuery()