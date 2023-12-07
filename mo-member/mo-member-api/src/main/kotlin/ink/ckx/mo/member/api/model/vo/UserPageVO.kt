package ink.ckx.mo.member.api.model.vo

import ink.ckx.mo.common.web.enums.GenderEnum
import ink.ckx.mo.common.web.enums.StatusEnum
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/18
 */
@Schema(description = "用户分页VO")
data class UserPageVO(

    @Schema(description = "用户id")
    var id: Long? = null,

    @Schema(description = "手机号")
    var mobile: String? = null,

    @Schema(description = "昵称")
    var nickName: String? = null,

    @Schema(description = "性别")
    var gender: GenderEnum? = null,

    @Schema(description = "生日")
    var birthday: LocalDate? = null,

    @Schema(description = "头像")
    var avatarUrl: String? = null,

    @Schema(description = "状态")
    var status: StatusEnum? = null,

    @Schema(description = "创建时间")
    var createTime: LocalDateTime? = null,
)