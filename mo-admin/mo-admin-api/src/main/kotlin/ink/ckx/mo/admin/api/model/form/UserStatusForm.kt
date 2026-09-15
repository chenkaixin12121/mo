package ink.ckx.mo.admin.api.model.form

import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

/**
 * 用户状态表单对象
 *
 * @author chenkaixin
 */
@Schema(description = "用户状态表单对象")
data class UserStatusForm(

    @field:NotNull(message = "状态不能为空")
    @Schema(description = "状态")
    var status: StatusEnum? = null,
)
