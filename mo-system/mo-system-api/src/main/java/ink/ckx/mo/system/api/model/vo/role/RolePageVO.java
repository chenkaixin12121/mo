package ink.ckx.mo.system.api.model.vo.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "角色分页VO")
@Data
public class RolePageVO {

    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色编码")
    private String code;

    @Schema(description = "排序")
    private Integer sort;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "状态")
    private StatusEnum status;
}