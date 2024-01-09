package ink.ckx.mo.admin.service.impl

import cn.hutool.core.lang.Assert
import cn.hutool.core.text.CharSequenceUtil
import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import ink.ckx.mo.admin.api.model.dto.SysUserInfoDTO
import ink.ckx.mo.admin.api.model.entity.SysUser
import ink.ckx.mo.admin.api.model.form.UserForm
import ink.ckx.mo.admin.api.model.query.UserPageQuery
import ink.ckx.mo.admin.api.model.vo.user.UserDetailVO
import ink.ckx.mo.admin.api.model.vo.user.UserExportVO
import ink.ckx.mo.admin.api.model.vo.user.UserLoginVO
import ink.ckx.mo.admin.api.model.vo.user.UserPageVO
import ink.ckx.mo.admin.converter.UserConverter
import ink.ckx.mo.admin.mapper.SysUserMapper
import ink.ckx.mo.admin.service.SysUserRoleService
import ink.ckx.mo.admin.service.SysUserService
import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.core.exception.BusinessException
import ink.ckx.mo.common.core.result.ResultCode
import ink.ckx.mo.common.security.util.getExp
import ink.ckx.mo.common.security.util.getJti
import ink.ckx.mo.common.security.util.getRoles
import ink.ckx.mo.common.security.util.getUserId
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

/**
 * 用户业务实现类
 *
 * @author chenkaixin
 */
@Service
class SysUserServiceImpl(
    val passwordEncoder: PasswordEncoder,
    val userRoleService: SysUserRoleService,
    val userConverter: UserConverter,
    val redisTemplate: RedisTemplate<String, String>,
) : ServiceImpl<SysUserMapper, SysUser>(), SysUserService {

    /**
     * 获取用户分页列表
     *
     * @param userPageQuery
     * @return
     */
    override fun listUserPages(userPageQuery: UserPageQuery): IPage<UserPageVO> {
        return baseMapper.listUserPages(
            Page(
                userPageQuery.pageNum,
                userPageQuery.pageSize
            ), userPageQuery
        )
    }

    /**
     * 获取用户详情
     *
     * @param userId
     * @return
     */
    override fun getUserDetail(userId: Long): UserDetailVO? {
        return baseMapper.getUserDetail(userId)
    }

    /**
     * 新增用户
     *
     * @param userForm 用户表单对象
     * @return
     */
    override fun saveUser(userForm: UserForm): Long? {
        val username = userForm.username
        val count = ktQuery().eq(SysUser::username, username).count()
        Assert.isFalse(count > 0) { BusinessException(ResultCode.PARAM_ERROR, "用户名已存在") }

        // 实体转换 form->entity
        val sysUser = userConverter.form2Entity(userForm)

        // 设置默认加密密码
        val defaultEncryptPwd = passwordEncoder.encode(CoreConstant.DEFAULT_USER_PASSWORD)
        sysUser.password = defaultEncryptPwd

        // 新增用户
        val result = save(sysUser)
        if (result) {
            // 保存用户角色
            userForm.roleIds?.let { userRoleService.saveUserRoles(sysUser.id, it) }
        }
        return sysUser.id
    }

    /**
     * 更新用户
     *
     * @param userId   用户ID
     * @param userForm 用户表单对象
     * @return
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun updateUser(userId: Long, userForm: UserForm) {
        val username = userForm.username
        val count = ktQuery().eq(SysUser::username, username).ne(SysUser::id, userId).count()
        Assert.isFalse(count > 0) { BusinessException(ResultCode.PARAM_ERROR, "用户名已存在") }

        // form -> entity
        val sysUser = userConverter.form2Entity(userForm)

        // 修改用户
        sysUser.id = userId
        val result = updateById(sysUser)
        if (result) {
            // 保存用户角色
            userForm.roleIds?.let { userRoleService.saveUserRoles(sysUser.id!!, it) }
        }
    }

    /**
     * 删除用户
     *
     * @param ids 用户ID，多个以英文逗号(,)分割
     * @return
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deleteUsers(ids: String) {
        val idList = ids.split(',').mapNotNull { it.toLongOrNull() }
        if (idList.isNotEmpty()) {
            this.removeByIds(idList)
        }
    }

    /**
     * 修改用户密码
     *
     * @param userId   用户ID
     * @param password 用户密码
     * @return
     */
    override fun updatePassword(userId: Long, password: String) {
        val encryptedPassword = passwordEncoder.encode(password)
        ktUpdate()
            .eq(SysUser::id, userId)
            .set(SysUser::password, encryptedPassword)
            .update()
    }

    /**
     * 根据用户名获取认证信息
     *
     * @param username
     * @return
     */
    override fun getUserAuthInfo(username: String): SysUserInfoDTO? {
        return baseMapper.getUserAuthInfo(username)
    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    override fun loginUserInfo(): UserLoginVO {
        // 登录用户entity
        val sysUser = ktQuery()
            .select(SysUser::id, SysUser::nickname, SysUser::avatar)
            .eq(SysUser::id, getUserId())
            .one()
        // entity -> VO
        val userLoginVO = userConverter.entity2LoginUser(sysUser)

        // 用户角色集合
        val roles = getRoles()
        userLoginVO.roles = roles

        // 用户权限集合
        userLoginVO.perms = if (roles.isNotEmpty()) {
            val cacheKeyList = roles.map { CoreConstant.ROLE_PERMS_CACHE_KEY_PREFIX + it }.toList()
            redisTemplate.opsForSet().union(cacheKeyList)
        } else {
            setOf()
        }
        return userLoginVO
    }

    override fun logout() {
        val jti = getJti()
        val expireTime = getExp()
        val currentTime = System.currentTimeMillis()
        // token未过期，添加至缓存作为黑名单限制访问，缓存时间为token过期剩余时间
        if (expireTime > currentTime) {
            redisTemplate.opsForValue()[CoreConstant.TOKEN_BLACK + jti, CharSequenceUtil.EMPTY, expireTime - currentTime] =
                TimeUnit.MILLISECONDS
        }
    }

    /**
     * 获取导出用户列表
     *
     * @param queryParams
     * @return
     */
    override fun listExportUsers(queryParams: UserPageQuery): List<UserExportVO> {
        return baseMapper.listExportUsers(queryParams)
    }
}