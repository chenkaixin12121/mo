package ink.ckx.mo.admin.api.model.vo.user

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 当前登录用户VO
 *
 * @author chenkaixin
 */
@Schema(description = "当前登录用户VO")
data class UserLoginVO(

    @Schema(description = "用户ID")
    var userId: Long? = null,

    @Schema(description = "用户昵称")
    var nickname: String? = null,

    @Schema(description = "头像地址")
    var avatar: String? = null,

    @Schema(description = "用户的角色编码集合")
    var roles: Set<String>? = null,

    @Schema(description = "用户的按钮权限标识集合")
    var perms: Set<String>? = null,
)