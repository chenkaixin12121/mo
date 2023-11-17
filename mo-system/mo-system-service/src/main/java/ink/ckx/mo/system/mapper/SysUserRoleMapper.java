package ink.ckx.mo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.ckx.mo.system.api.model.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色持久层
 *
 * @author chenkaixin
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}