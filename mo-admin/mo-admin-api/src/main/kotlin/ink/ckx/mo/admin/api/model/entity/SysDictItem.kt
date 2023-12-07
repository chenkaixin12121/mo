package ink.ckx.mo.admin.api.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import ink.ckx.mo.common.core.base.BaseEntity
import ink.ckx.mo.common.web.enums.StatusEnum

data class SysDictItem(
    @TableId(type = IdType.ASSIGN_ID)
    var id: Long? = null,
    var typeCode: String? = null,
    var name: String? = null,
    var value: String? = null,
    var status: StatusEnum? = null,
    var sort: Int? = null,
    var defaulted: Boolean? = null,
    var remark: String? = null,
) : BaseEntity() 