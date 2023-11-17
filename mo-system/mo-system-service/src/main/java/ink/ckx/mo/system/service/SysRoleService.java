package ink.ckx.mo.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import ink.ckx.mo.common.web.model.Option;
import ink.ckx.mo.system.api.model.entity.SysRole;
import ink.ckx.mo.system.api.model.form.RoleForm;
import ink.ckx.mo.system.api.model.query.RolePageQuery;
import ink.ckx.mo.system.api.model.vo.role.RoleDetailVO;
import ink.ckx.mo.system.api.model.vo.role.RolePageVO;

import java.util.List;

/**
 * 角色业务接口层
 *
 * @author chenkaixin
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 角色分页列表
     *
     * @param rolePageQuery
     * @return
     */
    Page<RolePageVO> listRolePages(RolePageQuery rolePageQuery);

    /**
     * 角色下拉列表
     *
     * @return
     */
    List<Option<Long>> listRoleOptions();

    /**
     * 获取角色详情
     *
     * @return
     */
    RoleDetailVO getRoleDetail(Long roleId);

    /**
     * 添加角色
     *
     * @param roleForm
     * @return
     */
    Long saveRole(RoleForm roleForm);

    /**
     * 更新角色
     *
     * @param roleId
     * @param roleForm
     * @return
     */
    void updateRole(Long roleId, RoleForm roleForm);

    /**
     * 修改角色状态
     *
     * @param roleId
     * @param status
     * @return
     */
    void updateRoleStatus(Long roleId, Integer status);

    /**
     * 批量删除角色
     *
     * @param ids
     * @return
     */
    void deleteRoles(String ids);

    /**
     * 获取角色的资源ID集合,资源包括菜单和权限
     *
     * @param roleId
     * @return
     */
    List<Long> getRoleMenuIds(Long roleId);

    /**
     * 修改角色的资源权限
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    void updateRoleMenus(Long roleId, List<Long> menuIds);
}