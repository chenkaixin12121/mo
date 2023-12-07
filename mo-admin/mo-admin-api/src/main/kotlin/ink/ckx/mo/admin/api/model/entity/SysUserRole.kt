package ink.ckx.mo.admin.api.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId

data class SysUserRole(
    @TableId(type = IdType.ASSIGN_ID)
    var id: Long? = null,
    var userId: Long? = null,
    var roleId: Long? = null,
) 