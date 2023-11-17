package ink.ckx.mo.system.api.model.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * 当前登录用户VO
 *
 * @author chenkaixin
 */
@Schema(description = "当前登录用户VO")
@Data
public class UserLoginVO {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "用户的角色编码集合")
    private Set<String> roles;

    @Schema(description = "用户的按钮权限标识集合")
    private Set<String> perms;
}