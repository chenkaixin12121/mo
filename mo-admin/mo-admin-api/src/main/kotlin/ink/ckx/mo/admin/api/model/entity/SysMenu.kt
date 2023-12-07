package ink.ckx.mo.admin.api.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import ink.ckx.mo.admin.api.enums.MenuTypeEnum
import ink.ckx.mo.common.core.base.BaseEntity
import ink.ckx.mo.common.web.enums.StatusEnum

/**
 * 菜单实体类
 *
 * @author chenkaixin
 */
data class SysMenu(
    @TableId(type = IdType.ASSIGN_ID)
    var id: Long? = null,
    var parentId: Long? = null,
    var treePath: String? = null,
    var name: String? = null,
    var type: MenuTypeEnum? = null,
    var path: String? = null,
    var component: String? = null,
    var perm: String? = null,
    var icon: String? = null,
    var sort: Int? = null,
    var alwaysShow: Int? = null,
    var visible: StatusEnum? = null,
) : BaseEntity()