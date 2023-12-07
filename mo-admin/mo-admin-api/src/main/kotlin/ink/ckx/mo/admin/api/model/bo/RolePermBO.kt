package ink.ckx.mo.admin.api.model.bo

import io.swagger.v3.oas.annotations.media.Schema

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/16
 */
@Schema(description = "角色权限BO")
data class RolePermBO(

    @Schema(description = "权限标识")
    var perm: String? = null,

    @Schema(description = "角色编码")
    var code: String? = null
)