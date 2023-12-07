package ink.ckx.mo.admin.api.model.vo.user

import ink.ckx.mo.common.web.enums.GenderEnum
import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * 用户分页VO
 *
 * @author chenkaixin
 */
@Schema(description = "用户分页VO")
data class UserPageVO(

    @Schema(description = "用户ID")
    var id: Long? = null,

    @Schema(description = "用户名")
    var username: String? = null,

    @Schema(description = "用户昵称")
    var nickname: String? = null,

    @Schema(description = "手机号")
    var mobile: String? = null,

    @Schema(description = "性别")
    var gender: GenderEnum? = null,

    @Schema(description = "用户头像地址")
    var avatar: String? = null,

    @Schema(description = "用户邮箱")
    var email: String? = null,

    @Schema(description = "用户状态Operation(summary = ")
    var status: StatusEnum? = null,

    @Schema(description = "部门名称")
    var deptName: String? = null,

    @Schema(description = "角色名称，多个使用英文逗号(,)分割")
    var roleNames: String? = null,

    @Schema(description = "创建时间")
    var createTime: LocalDateTime? = null,
)