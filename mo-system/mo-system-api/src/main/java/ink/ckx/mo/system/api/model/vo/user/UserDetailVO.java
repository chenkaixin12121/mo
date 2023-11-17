package ink.ckx.mo.system.api.model.vo.user;

import ink.ckx.mo.common.web.enums.GenderEnum;
import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 用户详情VO
 *
 * @author chenkaixin
 */
@Schema(description = "用户详情VO")
@Data
public class UserDetailVO {

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "姓别")
    private GenderEnum gender;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "邮箱")
    private String email;

    @NotBlank(message = "状态不能为空")
    @Schema(description = "状态")
    private StatusEnum status;

    @Schema(description = "部门id")
    private Long deptId;

    @Schema(description = "用户角色")
    private List<Long> roleIds;
}