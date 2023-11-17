package ink.ckx.mo.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import ink.ckx.mo.system.api.model.dto.SysUserInfoDTO;
import ink.ckx.mo.system.api.model.entity.SysUser;
import ink.ckx.mo.system.api.model.form.UserForm;
import ink.ckx.mo.system.api.model.query.UserPageQuery;
import ink.ckx.mo.system.api.model.vo.user.UserDetailVO;
import ink.ckx.mo.system.api.model.vo.user.UserExportVO;
import ink.ckx.mo.system.api.model.vo.user.UserLoginVO;
import ink.ckx.mo.system.api.model.vo.user.UserPageVO;

import java.util.List;

/**
 * 用户业务接口
 *
 * @author chenkaixin
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户分页列表
     *
     * @return
     */
    IPage<UserPageVO> listUserPages(UserPageQuery userPageQuery);

    /**
     * 获取用户详情
     *
     * @param userId
     * @return
     */
    UserDetailVO getUserDetail(Long userId);

    /**
     * 新增用户
     *
     * @param userForm 用户表单对象
     * @return
     */
    Long saveUser(UserForm userForm);

    /**
     * 修改用户
     *
     * @param userId   用户ID
     * @param userForm 用户表单对象
     * @return
     */
    void updateUser(Long userId, UserForm userForm);

    /**
     * 删除用户
     *
     * @param ids 用户ID，多个以英文逗号(,)分割
     * @return
     */
    void deleteUsers(String ids);

    /**
     * 修改用户密码
     *
     * @param userId   用户ID
     * @param password 用户密码
     * @return
     */
    void updatePassword(Long userId, String password);

    /**
     * 根据用户名获取认证信息
     *
     * @param username
     * @return
     */
    SysUserInfoDTO getUserAuthInfo(String username);

    /**
     * 获取登录用户信息
     *
     * @return
     */
    UserLoginVO getLoginUserInfo();

    /**
     * 登出
     */
    void logout();

    /**
     * 获取导出用户列表
     *
     * @param queryParams
     * @return
     */
    List<UserExportVO> listExportUsers(UserPageQuery queryParams);
}