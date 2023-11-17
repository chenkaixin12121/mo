package ink.ckx.mo.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ink.ckx.mo.system.api.model.entity.SysUserRole;

import java.util.List;

public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 保存用户角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    void saveUserRoles(Long userId, List<Long> roleIds);
}