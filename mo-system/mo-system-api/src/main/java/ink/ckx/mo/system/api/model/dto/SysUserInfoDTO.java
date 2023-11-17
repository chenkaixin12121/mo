package ink.ckx.mo.system.api.model.dto;

import ink.ckx.mo.common.web.enums.StatusEnum;
import lombok.Data;

import java.util.Set;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@Data
public class SysUserInfoDTO {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 用户id
     */
    private Integer dataScope;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户状态
     */
    private StatusEnum status;

    /**
     * 用户角色编码集合
     */
    private Set<String> roles;
}