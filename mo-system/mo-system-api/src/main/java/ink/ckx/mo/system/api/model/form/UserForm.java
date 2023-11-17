package ink.ckx.mo.system.api.model.form;

import ink.ckx.mo.common.web.enums.GenderEnum;
import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

/**
 * 用户表单对象
 *
 * @author chenkaixin
 */
@Schema(description = "用户表单对象")
@Data
public class UserForm {

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String username;

    @NotBlank(message = "用户昵称不能为空")
    @Schema(description = "用户昵称")
    private String nickname;

    @Pattern(regexp = "^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "手机号格式不正确")
    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "姓别")
    private GenderEnum gender;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "邮箱")
    private String email;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态")
    private StatusEnum status;

    @Schema(description = "部门id")
    private Long deptId;

    @NotEmpty(message = "用户角色不能为空")
    @Schema(description = "用户角色")
    private List<Long> roleIds;
}