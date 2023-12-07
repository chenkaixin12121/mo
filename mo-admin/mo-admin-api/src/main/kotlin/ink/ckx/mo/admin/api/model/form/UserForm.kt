package ink.ckx.mo.admin.api.model.form

import ink.ckx.mo.common.web.enums.GenderEnum
import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

/**
 * 用户表单对象
 *
 * @author chenkaixin
 */
@Schema(description = "用户表单对象")
data class UserForm(

    @field:NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    var username: String? = null,

    @field:NotBlank(message = "用户昵称不能为空")
    @Schema(description = "用户昵称")
    var nickname: String? = null,

    @Pattern(
        regexp = "^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$",
        message = "手机号格式不正确"
    )
    @Schema(description = "手机号")
    var mobile: String? = null,

    @Schema(description = "姓别")
    var gender: GenderEnum? = null,

    @Schema(description = "头像")
    var avatar: String? = null,

    @Schema(description = "邮箱")
    var email: String? = null,

    @field:NotNull(message = "状态不能为空")
    @Schema(description = "状态")
    var status: StatusEnum? = null,

    @Schema(description = "部门id")
    var deptId: Long? = null,

    @field:NotEmpty(message = "用户角色不能为空")
    @Schema(description = "用户角色")
    var roleIds: List<Long?>? = null,
)