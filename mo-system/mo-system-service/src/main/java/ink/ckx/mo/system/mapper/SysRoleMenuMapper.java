package ink.ckx.mo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.ckx.mo.system.api.model.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色<->菜单持久层
 *
 * @author chenkaixin
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * 获取角色拥有的菜单ID集合
     *
     * @param roleId
     * @return
     */
    List<Long> listMenuIdsByRoleId(Long roleId);
}