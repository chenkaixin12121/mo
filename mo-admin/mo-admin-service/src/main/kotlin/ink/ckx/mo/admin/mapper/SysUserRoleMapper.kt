package ink.ckx.mo.admin.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import ink.ckx.mo.admin.api.model.entity.SysUserRole
import org.apache.ibatis.annotations.Mapper

/**
 * 用户角色持久层
 *
 * @author chenkaixin
 */
@Mapper
interface SysUserRoleMapper : BaseMapper<SysUserRole>