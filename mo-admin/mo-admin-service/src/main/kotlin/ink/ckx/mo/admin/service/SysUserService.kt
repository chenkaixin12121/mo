package ink.ckx.mo.admin.service

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.service.IService
import ink.ckx.mo.admin.api.model.dto.SysUserInfoDTO
import ink.ckx.mo.admin.api.model.entity.SysUser
import ink.ckx.mo.admin.api.model.form.UserForm
import ink.ckx.mo.admin.api.model.query.UserPageQuery
import ink.ckx.mo.admin.api.model.vo.user.UserDetailVO
import ink.ckx.mo.admin.api.model.vo.user.UserExportVO
import ink.ckx.mo.admin.api.model.vo.user.UserLoginVO
import ink.ckx.mo.admin.api.model.vo.user.UserPageVO

/**
 * 用户业务接口
 *
 * @author chenkaixin
 */
interface SysUserService : IService<SysUser> {

    /**
     * 用户分页列表
     *
     * @return
     */
    fun listUserPages(userPageQuery: UserPageQuery): IPage<UserPageVO>

    /**
     * 获取用户详情
     *
     * @param userId
     * @return
     */
    fun getUserDetail(userId: Long): UserDetailVO?

    /**
     * 新增用户
     *
     * @param userForm 用户表单对象
     * @return
     */
    fun saveUser(userForm: UserForm): Long?

    /**
     * 修改用户
     *
     * @param userId   用户ID
     * @param userForm 用户表单对象
     * @return
     */
    fun updateUser(userId: Long, userForm: UserForm)

    /**
     * 删除用户
     *
     * @param ids 用户ID，多个以英文逗号(,)分割
     * @return
     */
    fun deleteUsers(ids: String)

    /**
     * 修改用户密码
     *
     * @param userId   用户ID
     * @param password 用户密码
     * @return
     */
    fun updatePassword(userId: Long, password: String)

    /**
     * 根据用户名获取认证信息
     *
     * @param username
     * @return
     */
    fun getUserAuthInfo(username: String): SysUserInfoDTO?

    /**
     * 获取登录用户信息
     *
     * @return
     */
    fun loginUserInfo(): UserLoginVO

    /**
     * 登出
     */
    fun logout()

    /**
     * 获取导出用户列表
     *
     * @param queryParams
     * @return
     */
    fun listExportUsers(queryParams: UserPageQuery): List<UserExportVO>
}