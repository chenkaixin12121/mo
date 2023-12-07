package ink.ckx.mo.common.security.util

import cn.hutool.core.convert.Convert
import cn.hutool.json.JSONUtil
import com.fasterxml.jackson.databind.ObjectMapper
import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.core.result.Result
import ink.ckx.mo.common.core.result.ResultCode
import jakarta.servlet.ServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

fun getDeptId(): Int {
    return Convert.toInt(getTokenAttributes()[CoreConstant.DEPT_ID])
}

fun getDataScope(): Int {
    return Convert.toInt(getTokenAttributes()[CoreConstant.DATA_SCOPE])
}

fun getExp(): Int {
    return Convert.toInt(getTokenAttributes()[JwtClaimNames.EXP])
}

fun getJti(): String {
    return getTokenAttributes()[JwtClaimNames.JTI].toString()
}

fun getAud(): String? {
    val tokenAttributes = getTokenAttributes()
    if (tokenAttributes.isEmpty()) {
        return null
    }
    val aud = JSONUtil.toJsonStr(tokenAttributes[JwtClaimNames.AUD])
    return JSONUtil.toList(aud, String::class.java)[0]
}

/**
 * 获取用户id
 *
 * @return
 */
fun getUserId(): Int {
    return Convert.toInt(getTokenAttributes()[CoreConstant.USER_ID])
}

/**
 * 是否超级管理员
 *
 * @return
 */
fun isSuperAdmin(): Boolean {
    return getRoles().contains(CoreConstant.SUPER_ADMIN_CODE)
}

/**
 * 获取用户角色集合
 */
fun getRoles(): Set<String> {
    val authentication: Authentication = SecurityContextHolder.getContext().authentication ?: return emptySet()
    return AuthorityUtils.authorityListToSet(authentication.authorities);
}

private fun getTokenAttributes(): Map<String, Any> {
    val authentication: Authentication = SecurityContextHolder.getContext().authentication ?: return emptyMap()
    return when (authentication) {
        is JwtAuthenticationToken -> authentication.tokenAttributes ?: emptyMap()
        else -> emptyMap()
    }
}


object SecurityUtil {

    fun fail(response: ServletResponse, resultCode: ResultCode) {
        response.contentType = "application/json"
        val mapper = ObjectMapper()
        val result: Result<Void> = Result.fail(resultCode)
        mapper.writeValue(response.outputStream, result)
    }
}