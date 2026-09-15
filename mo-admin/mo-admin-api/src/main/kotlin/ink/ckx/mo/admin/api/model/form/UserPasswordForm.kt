package ink.ckx.mo.admin.api.model.form

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 用户密码表单对象
 *
 * @author chenkaixin
 */
@Schema(description = "用户密码表单对象")
data class UserPasswordForm(

    @field:NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    var password: String? = null,
)
