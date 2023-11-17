package ink.ckx.mo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.ckx.mo.system.api.model.bo.RouteBO;
import ink.ckx.mo.system.api.model.bo.RolePermBO;
import ink.ckx.mo.system.api.model.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author chenkaixin
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 获取路由列表
     *
     * @return
     */
    List<RouteBO> listRoutes();

    /**
     * 权限列表
     *
     * @return
     */
    List<RolePermBO> listPermission();
}