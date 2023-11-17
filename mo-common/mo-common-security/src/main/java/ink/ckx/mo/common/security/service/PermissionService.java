package ink.ckx.mo.common.security.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.PatternMatchUtils;

import java.util.Set;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/12
 */
@Service("pms")
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 判断当前登录用户是否拥有操作权限
     *
     * @param perm 权限标识
     * @return
     */
    public boolean hasPerm(String perm) {
        // 超级管理员放行
        if (SecurityUtil.isSuperAdmin()) {
            return true;
        }
        // 校验权限
        if (CharSequenceUtil.isBlank(perm)) {
            return false;
        }
        Set<String> roles = SecurityUtil.getRoles();
        if (CollUtil.isEmpty(roles)) {
            return false;
        }
        Set<String> perms = redisTemplate.opsForSet().union(roles.stream().map(e -> CoreConstant.ROLE_PERMS_CACHE_KEY_PREFIX + e).toList());
        if (CollUtil.isEmpty(perms)) {
            return false;
        }
        return perms.stream().anyMatch(item -> PatternMatchUtils.simpleMatch(perm, item));
    }
}