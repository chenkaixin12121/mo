package ink.ckx.mo.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.core.exception.BusinessException;
import ink.ckx.mo.common.core.result.ResultCode;
import ink.ckx.mo.common.security.util.SecurityUtil;
import ink.ckx.mo.system.api.model.dto.SysUserInfoDTO;
import ink.ckx.mo.system.api.model.entity.SysUser;
import ink.ckx.mo.system.api.model.form.UserForm;
import ink.ckx.mo.system.api.model.query.UserPageQuery;
import ink.ckx.mo.system.api.model.vo.user.UserDetailVO;
import ink.ckx.mo.system.api.model.vo.user.UserExportVO;
import ink.ckx.mo.system.api.model.vo.user.UserLoginVO;
import ink.ckx.mo.system.api.model.vo.user.UserPageVO;
import ink.ckx.mo.system.converter.UserConverter;
import ink.ckx.mo.system.mapper.SysUserMapper;
import ink.ckx.mo.system.service.SysUserRoleService;
import ink.ckx.mo.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户业务实现类
 *
 * @author chenkaixin
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final PasswordEncoder passwordEncoder;

    private final SysUserRoleService userRoleService;

    private final UserConverter userConverter;

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 获取用户分页列表
     *
     * @param userPageQuery
     * @return
     */
    @Override
    public IPage<UserPageVO> listUserPages(UserPageQuery userPageQuery) {
        return this.baseMapper.listUserPages(new Page<>(userPageQuery.getPageNum(),
                userPageQuery.getPageSize()), userPageQuery);
    }

    /**
     * 获取用户详情
     *
     * @param userId
     * @return
     */
    @Override
    public UserDetailVO getUserDetail(Long userId) {
        return this.baseMapper.getUserDetail(userId);
    }

    /**
     * 新增用户
     *
     * @param userForm 用户表单对象
     * @return
     */
    @Override
    public Long saveUser(UserForm userForm) {
        String username = userForm.getUsername();

        long count = this.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        Assert.isFalse(count > 0, () -> new BusinessException(ResultCode.PARAM_ERROR, "用户名已存在"));

        // 实体转换 form->entity
        SysUser sysUser = userConverter.form2Entity(userForm);

        // 设置默认加密密码
        String defaultEncryptPwd = passwordEncoder.encode(CoreConstant.DEFAULT_USER_PASSWORD);
        sysUser.setPassword(defaultEncryptPwd);

        // 新增用户
        boolean result = this.save(sysUser);
        if (result) {
            // 保存用户角色
            userRoleService.saveUserRoles(sysUser.getId(), userForm.getRoleIds());
        }
        return sysUser.getId();
    }

    /**
     * 更新用户
     *
     * @param userId   用户ID
     * @param userForm 用户表单对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(Long userId, UserForm userForm) {
        String username = userForm.getUsername();

        long count = this.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
                .ne(SysUser::getId, userId));
        Assert.isFalse(count > 0, () -> new BusinessException(ResultCode.PARAM_ERROR, "用户名已存在"));

        // form -> entity
        SysUser sysUser = userConverter.form2Entity(userForm);

        // 修改用户
        boolean result = this.updateById(sysUser);
        sysUser.setId(userId);
        if (result) {
            // 保存用户角色
            userRoleService.saveUserRoles(sysUser.getId(), userForm.getRoleIds());
        }
    }

    /**
     * 删除用户
     *
     * @param ids 用户ID，多个以英文逗号(,)分割
     * @return
     */
    @Override
    public void deleteUsers(String ids) {
        List<Long> idList = StrUtil.split(ids, ',', 0, true, Long::valueOf);
        if (CollUtil.isNotEmpty(idList)) {
            this.removeByIds(idList);
        }
    }

    /**
     * 修改用户密码
     *
     * @param userId   用户ID
     * @param password 用户密码
     * @return
     */
    @Override
    public void updatePassword(Long userId, String password) {
        String encryptedPassword = passwordEncoder.encode(password);

        this.update(new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getId, userId)
                .set(SysUser::getPassword, encryptedPassword));
    }

    /**
     * 根据用户名获取认证信息
     *
     * @param username
     * @return
     */
    @Override
    public SysUserInfoDTO getUserAuthInfo(String username) {
        return this.baseMapper.getUserAuthInfo(username);
    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    @Override
    public UserLoginVO getLoginUserInfo() {
        // 登录用户entity
        SysUser sysUser = this.getOne(new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getId, SysUser::getNickname, SysUser::getAvatar)
                .eq(SysUser::getId, SecurityUtil.getUserId()));
        // entity -> VO
        UserLoginVO userLoginVO = userConverter.entity2LoginUser(sysUser);

        // 用户角色集合
        Set<String> roles = SecurityUtil.getRoles();
        userLoginVO.setRoles(roles);

        // 用户权限集合
        Set<String> perms = new HashSet<>();
        if (CollUtil.isNotEmpty(roles)) {
            List<String> cacheKeyList = roles.stream().map(e -> CoreConstant.ROLE_PERMS_CACHE_KEY_PREFIX + e).toList();
            perms = redisTemplate.opsForSet().union(cacheKeyList);
        }
        userLoginVO.setPerms(perms);

        return userLoginVO;
    }

    @Override
    public void logout() {
        String jti = SecurityUtil.getJti();
        Long expireTime = SecurityUtil.getExp();

        long currentTime = System.currentTimeMillis();
        if (expireTime != null) {
            // token未过期，添加至缓存作为黑名单限制访问，缓存时间为token过期剩余时间
            if (expireTime > currentTime) {
                redisTemplate.opsForValue().set(CoreConstant.TOKEN_BLACK + jti, CharSequenceUtil.EMPTY, (expireTime - currentTime), TimeUnit.MILLISECONDS);
            }
        } else {
            // token 永不过期则永久加入黑名单（一般不会有）
            redisTemplate.opsForValue().set(CoreConstant.TOKEN_BLACK + jti, CharSequenceUtil.EMPTY);
        }
    }

    /**
     * 获取导出用户列表
     *
     * @param queryParams
     * @return
     */
    @Override
    public List<UserExportVO> listExportUsers(UserPageQuery queryParams) {
        return this.baseMapper.listExportUsers(queryParams);
    }
}