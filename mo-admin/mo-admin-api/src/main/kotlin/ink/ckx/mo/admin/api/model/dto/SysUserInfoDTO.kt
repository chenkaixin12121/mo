package ink.ckx.mo.admin.api.model.dto

import ink.ckx.mo.common.mybatis.enums.DataScopeEnum
import ink.ckx.mo.common.web.enums.StatusEnum

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
data class SysUserInfoDTO(

    /**
     * 用户id
     */
    var userId: Long? = null,

    /**
     * 部门id
     */
    var deptId: Long? = null,

    /**
     * 数据权限
     */
    var dataScope: DataScopeEnum? = null,

    /**
     * 用户名
     */
    var username: String? = null,

    /**
     * 用户密码
     */
    var password: String? = null,

    /**
     * 用户状态
     */
    var status: StatusEnum? = null,

    /**
     * 用户角色编码集合
     */
    var roles: Set<String>? = null,
)