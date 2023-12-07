package ink.ckx.mo.admin.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import ink.ckx.mo.admin.api.model.entity.SysRoleMenu
import org.apache.ibatis.annotations.Mapper

/**
 * 角色<->菜单持久层
 *
 * @author chenkaixin
 */
@Mapper
interface SysRoleMenuMapper : BaseMapper<SysRoleMenu> {

    /**
     * 获取角色拥有的菜单ID集合
     *
     * @param roleId
     * @return
     */
    fun listMenuIdsByRoleId(roleId: Long): List<Long>
}