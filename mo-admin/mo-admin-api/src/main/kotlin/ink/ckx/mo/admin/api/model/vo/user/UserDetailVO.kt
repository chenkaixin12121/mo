package ink.ckx.mo.admin.api.model.vo.user

import ink.ckx.mo.common.web.enums.GenderEnum
import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 用户详情VO
 *
 * @author chenkaixin
 */
@Schema(description = "用户详情VO")
data class UserDetailVO(

    @Schema(description = "用户id")
    var id: Long? = null,

    @Schema(description = "用户名")
    var username: String? = null,

    @Schema(description = "用户昵称")
    var nickname: String? = null,

    @Schema(description = "手机号")
    var mobile: String? = null,

    @Schema(description = "姓别")
    var gender: GenderEnum? = null,

    @Schema(description = "头像")
    var avatar: String? = null,

    @Schema(description = "邮箱")
    var email: String? = null,

    @field:NotBlank(message = "状态不能为空")
    @Schema(description = "状态")
    var status: StatusEnum? = null,

    @Schema(description = "部门id")
    var deptId: Long? = null,

    @Schema(description = "用户角色")
    var roleIds: List<Long>? = null,
)