package ink.ckx.mo.admin.api.model.query

import ink.ckx.mo.common.core.base.BasePageQuery
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "字典类型分页查询对象")
data class DictTypePageQuery(

    @Schema(description = "关键字(类型名称/类型编码)")
    var keywords: String? = null
) : BasePageQuery()