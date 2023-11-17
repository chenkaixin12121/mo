package ink.ckx.mo.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.core.exception.BusinessException;
import ink.ckx.mo.common.core.result.ResultCode;
import ink.ckx.mo.common.security.util.SecurityUtil;
import ink.ckx.mo.common.web.model.Option;
import ink.ckx.mo.system.api.model.entity.SysRole;
import ink.ckx.mo.system.api.model.entity.SysRoleMenu;
import ink.ckx.mo.system.api.model.entity.SysUserRole;
import ink.ckx.mo.system.api.model.form.RoleForm;
import ink.ckx.mo.system.api.model.query.RolePageQuery;
import ink.ckx.mo.system.api.model.vo.role.RoleDetailVO;
import ink.ckx.mo.system.api.model.vo.role.RolePageVO;
import ink.ckx.mo.system.converter.RoleConverter;
import ink.ckx.mo.system.mapper.SysRoleMapper;
import ink.ckx.mo.system.service.SysMenuService;
import ink.ckx.mo.system.service.SysRoleMenuService;
import ink.ckx.mo.system.service.SysRoleService;
import ink.ckx.mo.system.service.SysUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色业务实现类
 *
 * @author chenkaixin
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMenuService sysRoleMenuService;

    private final SysUserRoleService sysUserRoleService;

    private final RoleConverter roleConverter;

    private final SysMenuService menuService;

    /**
     * 角色分页列表
     *
     * @param rolePageQuery
     * @return
     */
    @Override
    public Page<RolePageVO> listRolePages(RolePageQuery rolePageQuery) {
        // 查询参数
        int pageNum = rolePageQuery.getPageNum();
        int pageSize = rolePageQuery.getPageSize();
        String keywords = rolePageQuery.getKeywords();

        // 查询数据
        Page<SysRole> rolePage = this.page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<SysRole>()
                        .and(StrUtil.isNotBlank(keywords), wrapper -> wrapper.like(StrUtil.isNotBlank(keywords),
                                SysRole::getName, keywords).or().like(StrUtil.isNotBlank(keywords),
                                SysRole::getCode, keywords))
                        // 非超级管理员不显示超级管理员角色
                        .ne(!SecurityUtil.isSuperAdmin(), SysRole::getCode, CoreConstant.SUPER_ADMIN_CODE)
        );

        // 实体转换
        return roleConverter.entity2Page(rolePage);
    }

    /**
     * 角色下拉列表
     *
     * @return
     */
    @Override
    public List<Option<Long>> listRoleOptions() {
        // 查询数据
        List<SysRole> roleList = this.list(new LambdaQueryWrapper<SysRole>()
                .select(SysRole::getId, SysRole::getName)
                .ne(!SecurityUtil.isSuperAdmin(), SysRole::getCode, CoreConstant.SUPER_ADMIN_CODE)
                .orderByAsc(SysRole::getSort));

        // 实体转换
        return roleConverter.roles2Options(roleList);
    }

    @Override
    public RoleDetailVO getRoleDetail(Long roleId) {
        SysRole entity = this.getOne(new LambdaQueryWrapper<SysRole>()
                .select(SysRole::getId, SysRole::getDataScope, SysRole::getName, SysRole::getCode, SysRole::getSort, SysRole::getStatus)
                .eq(SysRole::getId, roleId));

        return roleConverter.entity2DetailVO(entity);
    }

    /**
     * @param roleForm
     * @return
     */
    @Override
    public Long saveRole(RoleForm roleForm) {
        String roleCode = roleForm.getCode();

        long count = this.count(new LambdaQueryWrapper<SysRole>()
                .and(wrapper -> wrapper.eq(SysRole::getCode, roleCode)
                        .or()
                        .eq(SysRole::getName, roleCode)));
        Assert.isFalse(count > 0, () -> new BusinessException(ResultCode.PARAM_ERROR, "角色名称或角色编码重复，请检查！"));

        // 实体转换
        SysRole sysRole = roleConverter.form2Entity(roleForm);

        boolean result = this.save(sysRole);
        if (result) {
            menuService.refreshRolePerm();
        }
        return sysRole.getId();
    }

    @Override
    public void updateRole(Long id, RoleForm roleForm) {
        String roleCode = roleForm.getCode();

        long count = this.count(new LambdaQueryWrapper<SysRole>()
                .ne(SysRole::getId, id)
                .and(wrapper -> wrapper.eq(SysRole::getCode, roleCode)
                        .or()
                        .eq(SysRole::getName, roleCode)));
        Assert.isFalse(count > 0, () -> new BusinessException(ResultCode.PARAM_ERROR, "角色名称或角色编码重复，请检查！"));

        // 实体转换
        SysRole sysRole = roleConverter.form2Entity(roleForm);
        sysRole.setId(id);
        boolean result = this.updateById(sysRole);
        if (result) {
            menuService.refreshRolePerm();
        }
    }

    /**
     * 修改角色状态
     *
     * @param roleId
     * @param status
     * @return
     */
    @Override
    public void updateRoleStatus(Long roleId, Integer status) {
        boolean result = this.update(new LambdaUpdateWrapper<SysRole>()
                .eq(SysRole::getId, roleId).set(SysRole::getStatus, status));
        if (result) {
            menuService.refreshRolePerm();
        }
    }

    /**
     * 批量删除角色
     *
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRoles(String ids) {
        List<Long> roleIdList = StrUtil.split(ids, ',', 0, true, Long::valueOf);
        if (CollUtil.isEmpty(roleIdList)) {
            return;
        }
        roleIdList.forEach(id -> {
            long count = sysUserRoleService.count(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, id));
            Assert.isFalse(count > 0, () -> new BusinessException(ResultCode.PARAM_ERROR, "该角色已分配用户，无法删除"));
            sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
        });

        boolean result = this.removeByIds(roleIdList);
        if (result) {
            menuService.refreshRolePerm();
        }
    }

    /**
     * 获取角色的资源ID集合,资源包括菜单和权限
     *
     * @param roleId
     * @return
     */
    @Override
    public List<Long> getRoleMenuIds(Long roleId) {
        // 获取角色拥有的菜单ID集合
        return sysRoleMenuService.listMenuIdsByRoleId(roleId);
    }

    /**
     * 修改角色的资源权限
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleMenus(Long roleId, List<Long> menuIds) {
        // 删除角色菜单
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        // 新增角色菜单
        if (CollUtil.isNotEmpty(menuIds)) {
            List<SysRoleMenu> roleMenus = menuIds.stream()
                    .map(menuId -> new SysRoleMenu(null, roleId, menuId)).collect(Collectors.toList());
            sysRoleMenuService.saveBatch(roleMenus);
        }
        menuService.refreshRolePerm();
    }
}