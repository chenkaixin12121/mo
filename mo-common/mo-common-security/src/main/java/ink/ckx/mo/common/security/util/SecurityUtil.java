package ink.ckx.mo.common.security.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.core.result.Result;
import ink.ckx.mo.common.core.result.ResultCode;
import jakarta.servlet.ServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/12
 */
public class SecurityUtil {

    public static Long getDeptId() {
        return Convert.toLong(getTokenAttributes().get(CoreConstant.DEPT_ID));
    }

    public static Integer getDataScope() {
        return Convert.toInt(getTokenAttributes().get(CoreConstant.DATA_SCOPE));
    }

    public static Long getExp() {
        return Convert.toLong(getTokenAttributes().get(JwtClaimNames.EXP));
    }

    public static String getJti() {
        return String.valueOf(getTokenAttributes().get(JwtClaimNames.JTI));
    }

    public static String getAud() {
        Map<String, Object> tokenAttributes = getTokenAttributes();
        if (MapUtil.isEmpty(tokenAttributes)) {
            return null;
        }
        String aud = JSONUtil.toJsonStr(tokenAttributes.get(JwtClaimNames.AUD));
        return JSONUtil.toList(aud, String.class).get(0);
    }

    /**
     * 获取用户id
     *
     * @return
     */
    public static Long getUserId() {
        return Convert.toLong(getTokenAttributes().get(CoreConstant.USER_ID));
    }

    /**
     * 是否超级管理员
     *
     * @return
     */
    public static boolean isSuperAdmin() {
        return getRoles().contains(CoreConstant.SUPER_ADMIN_CODE);
    }

    /**
     * 获取用户角色集合
     */
    public static Set<String> getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Collections.emptySet();
        }
        return AuthorityUtils.authorityListToSet(authentication.getAuthorities())
                .stream()
                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
    }

    public static Map<String, Object> getTokenAttributes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Collections.emptyMap();
        }
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return jwtAuthenticationToken.getTokenAttributes();
        }
        return Collections.emptyMap();
    }

    @SneakyThrows
    public static void fail(ServletResponse response, ResultCode resultCode) {
        response.setContentType("application/json");
        com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), Result.fail(resultCode));
    }
}