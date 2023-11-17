package ink.ckx.mo.member.api.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import ink.ckx.mo.common.web.enums.GenderEnum;
import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/18
 */
@Schema(description = "用户分页VO")
@Data
public class UserPageVO {

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "性别")
    private GenderEnum gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "生日")
    private LocalDate birthday;

    @Schema(description = "头像")
    private String avatarUrl;

    @Schema(description = "状态")
    private StatusEnum status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
