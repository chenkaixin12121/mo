package ink.ckx.mo.member.api.model.dto

import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@Schema(description = "会员用户信息DTO")
data class MemberUserInfoDTO(

    @Schema(description = "用户id")
    var userId: Long? = null,

    @Schema(description = "手机号")
    var mobile: String? = null,

    @Schema(description = "状态")
    var status: StatusEnum? = null,
)