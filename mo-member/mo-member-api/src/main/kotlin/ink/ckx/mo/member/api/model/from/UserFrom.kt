package ink.ckx.mo.member.api.model.from

import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/18
 */
@Schema(description = "用户表单对象")
data class UserFrom(

    @field:NotBlank(message = "手机号不能为空")
    @Schema(description = "手机号")
    var mobile: String? = null,

    @field:NotBlank(message = "昵称不能为空")
    @Schema(description = "昵称")
    var nickName: String? = null,

    @field:NotNull(message = "状态不能为空")
    @Schema(description = "状态")
    var status: StatusEnum? = null,
)