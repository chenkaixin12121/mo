package ink.ckx.mo.admin.api.model.query

import ink.ckx.mo.common.core.base.BasePageQuery
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "字典数据项分页查询对象")
data class DictItemPageQuery(

    @Schema(description = "关键字(字典项名称)")
    var keywords: String? = null,

    @Schema(description = "字典类型编码")
    var typeCode: String? = null,
) : BasePageQuery() 