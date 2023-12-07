package ink.ckx.mo.admin.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import ink.ckx.mo.admin.api.model.bo.RolePermBO
import ink.ckx.mo.admin.api.model.bo.RouteBO
import ink.ckx.mo.admin.api.model.entity.SysMenu
import org.apache.ibatis.annotations.Mapper

/**
 * @author chenkaixin
 */
@Mapper
interface SysMenuMapper : BaseMapper<SysMenu> {

    /**
     * 获取路由列表
     *
     * @return
     */
    fun listRoutes(): List<RouteBO>

    /**
     * 权限列表
     *
     * @return
     */
    fun listPermission(): List<RolePermBO>
}