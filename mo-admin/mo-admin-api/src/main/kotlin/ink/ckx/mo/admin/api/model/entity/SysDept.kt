package ink.ckx.mo.admin.api.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import ink.ckx.mo.common.core.base.BaseEntity
import ink.ckx.mo.common.web.enums.StatusEnum

data class SysDept(
    @TableId(type = IdType.ASSIGN_ID)
    var id: Long? = null,
    var name: String? = null,
    var parentId: Long? = null,
    var treePath: String? = null,
    var sort: Int? = null,
    var status: StatusEnum? = null,
) : BaseEntity() 