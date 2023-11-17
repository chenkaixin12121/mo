package ink.ckx.mo.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.ckx.mo.system.api.model.entity.SysUserRole;
import ink.ckx.mo.system.mapper.SysUserRoleMapper;
import ink.ckx.mo.system.service.SysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    /**
     * 保存用户角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @Override
    public void saveUserRoles(Long userId, List<Long> roleIds) {
        if (userId == null || CollUtil.isEmpty(roleIds)) {
            return;
        }

        // 用户原角色ID集合
        List<Long> userRoleIds = this.list(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId)).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        // 新增用户角色
        List<Long> saveRoleIds;
        if (CollUtil.isEmpty(userRoleIds)) {
            saveRoleIds = roleIds;
        } else {
            saveRoleIds = roleIds.stream().filter(roleId -> !userRoleIds.contains(roleId)).collect(Collectors.toList());
        }

        List<SysUserRole> saveUserRoles = saveRoleIds.stream()
                .map(roleId -> new SysUserRole(null, userId, roleId)).collect(Collectors.toList());
        this.saveBatch(saveUserRoles);

        // 删除用户角色
        if (CollUtil.isNotEmpty(userRoleIds)) {
            List<Long> removeRoleIds = userRoleIds.stream().filter(roleId -> !roleIds.contains(roleId)).collect(Collectors.toList());

            if (CollUtil.isNotEmpty(removeRoleIds)) {
                this.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId).in(SysUserRole::getRoleId, removeRoleIds));
            }
        }
    }
}