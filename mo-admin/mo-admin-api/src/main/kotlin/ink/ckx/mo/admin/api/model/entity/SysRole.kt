package ink.ckx.mo.admin.api.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import ink.ckx.mo.common.core.base.BaseEntity
import ink.ckx.mo.common.web.enums.StatusEnum

data class SysRole(
    @TableId(type = IdType.ASSIGN_ID)
    var id: Long? = null,
    var name: String? = null,
    var code: String? = null,
    var sort: Int? = null,
    var dataScope: Int? = null,
    var status: StatusEnum? = null,
) : BaseEntity()