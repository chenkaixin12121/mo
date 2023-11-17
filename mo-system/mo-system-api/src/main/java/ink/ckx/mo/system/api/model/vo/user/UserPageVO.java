package ink.ckx.mo.system.api.model.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import ink.ckx.mo.common.web.enums.GenderEnum;
import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户分页VO
 *
 * @author chenkaixin
 */
@Schema(description = "用户分页VO")
@Data
public class UserPageVO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "性别")
    private GenderEnum gender;

    @Schema(description = "用户头像地址")
    private String avatar;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "用户状态Operation(summary = ")
    private StatusEnum status;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "角色名称，多个使用英文逗号(,)分割")
    private String roleNames;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}