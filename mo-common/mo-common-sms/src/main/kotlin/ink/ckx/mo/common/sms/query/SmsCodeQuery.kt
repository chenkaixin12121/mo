package ink.ckx.mo.common.sms.query

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
@Schema(description = "短信验证码Query")
data class SmsCodeQuery(

    @field:NotBlank(message = "手机号不能为空")
    @Schema(description = "手机号")
    var phoneNumber: String? = null,
)