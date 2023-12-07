package ink.ckx.mo.admin.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import ink.ckx.mo.admin.api.model.entity.SysRoleMenu
import ink.ckx.mo.admin.mapper.SysRoleMenuMapper
import ink.ckx.mo.admin.service.SysRoleMenuService
import org.springframework.stereotype.Service

@Service
class SysRoleMenuServiceImpl : ServiceImpl<SysRoleMenuMapper, SysRoleMenu>(), SysRoleMenuService {

    /**
     * 获取角色拥有的菜单ID集合
     *
     * @param roleId
     * @return
     */
    override fun listMenuIdsByRoleId(roleId: Long): List<Long> {
        return baseMapper.listMenuIdsByRoleId(roleId)
    }
}