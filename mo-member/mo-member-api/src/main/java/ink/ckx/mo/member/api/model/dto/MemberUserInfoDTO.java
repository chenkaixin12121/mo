package ink.ckx.mo.member.api.model.dto;

import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@Schema(description = "会员用户信息DTO")
@Data
public class MemberUserInfoDTO {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "状态")
    private StatusEnum status;
}