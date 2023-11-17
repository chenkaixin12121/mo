package ink.ckx.mo.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.ckx.mo.system.api.model.entity.SysRoleMenu;
import ink.ckx.mo.system.mapper.SysRoleMenuMapper;
import ink.ckx.mo.system.service.SysRoleMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    /**
     * 获取角色拥有的菜单ID集合
     *
     * @param roleId
     * @return
     */
    @Override
    public List<Long> listMenuIdsByRoleId(Long roleId) {
        return this.baseMapper.listMenuIdsByRoleId(roleId);
    }
}