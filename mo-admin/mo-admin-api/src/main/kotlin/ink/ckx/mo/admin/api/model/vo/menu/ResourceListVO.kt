package ink.ckx.mo.admin.api.model.vo.menu

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "资源列表VO")
data class ResourceListVO(

    @Schema(description = "选项的值")
    var value: Any? = null,

    @Schema(description = "选项的标签")
    var label: String? = null,

    @Schema(description = "子菜单")
    var children: List<ResourceListVO>? = null,
)