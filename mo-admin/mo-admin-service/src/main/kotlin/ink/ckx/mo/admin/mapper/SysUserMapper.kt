package ink.ckx.mo.admin.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import ink.ckx.mo.admin.api.model.dto.SysUserInfoDTO
import ink.ckx.mo.admin.api.model.entity.SysUser
import ink.ckx.mo.admin.api.model.query.UserPageQuery
import ink.ckx.mo.admin.api.model.vo.user.UserDetailVO
import ink.ckx.mo.admin.api.model.vo.user.UserExportVO
import ink.ckx.mo.admin.api.model.vo.user.UserPageVO
import ink.ckx.mo.common.mybatis.annotation.DataPermission
import org.apache.ibatis.annotations.Mapper

/**
 * 用户持久层
 *
 * @author chenkaixin
 */
@Mapper
interface SysUserMapper : BaseMapper<SysUser> {

    /**
     * 获取用户分页列表
     *
     * @param page
     * @param queryParams 查询参数
     * @return
     */
    @DataPermission(deptAlias = "u")
    fun listUserPages(page: Page<UserPageVO>, queryParams: UserPageQuery): Page<UserPageVO>

    /**
     * 获取用户表单详情
     *
     * @param userId 用户ID
     * @return
     */
    fun getUserDetail(userId: Long): UserDetailVO?

    /**
     * 根据用户名获取认证信息
     *
     * @param username
     * @return
     */
    fun getUserAuthInfo(username: String): SysUserInfoDTO?

    /**
     * 获取导出用户列表
     *
     * @param queryParams
     * @return
     */
    @DataPermission(deptAlias = "u")
    fun listExportUsers(queryParams: UserPageQuery): List<UserExportVO>
}