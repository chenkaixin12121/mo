package ink.ckx.mo.admin.api.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId

data class SysRoleMenu(
    @TableId(type = IdType.ASSIGN_ID)
    var id: Long? = null,
    var roleId: Long? = null,
    var menuId: Long? = null,
) 