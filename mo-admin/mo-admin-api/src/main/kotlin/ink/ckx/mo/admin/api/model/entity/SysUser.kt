package ink.ckx.mo.admin.api.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import ink.ckx.mo.common.core.base.BaseEntity
import ink.ckx.mo.common.web.enums.GenderEnum
import ink.ckx.mo.common.web.enums.StatusEnum

data class SysUser(
    @TableId(type = IdType.ASSIGN_ID)
    var id: Long? = null,
    var username: String? = null,
    var nickname: String? = null,
    var gender: GenderEnum? = null,
    var password: String? = null,
    var deptId: Long? = null,
    var avatar: String? = null,
    var mobile: String? = null,
    var email: String? = null,
    var status: StatusEnum? = null,
) : BaseEntity()