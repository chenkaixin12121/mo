package ink.ckx.mo.member.api.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import ink.ckx.mo.common.core.base.BaseEntity
import ink.ckx.mo.common.web.enums.GenderEnum
import ink.ckx.mo.common.web.enums.StatusEnum
import java.time.LocalDate

data class MemberUser(
    @TableId(type = IdType.ASSIGN_ID)
    var id: Long? = null,
    var mobile: String? = null,
    var nickName: String? = null,
    var gender: GenderEnum? = null,
    var birthday: LocalDate? = null,
    var avatarUrl: String? = null,
    var openid: String? = null,
    var status: StatusEnum? = null,
) : BaseEntity()