package ink.ckx.mo.system.api.model.vo.role;

import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/15
 */
@Schema(description = "角色详情VO")
@Data
public class RoleDetailVO {

    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色编码")
    private String code;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "数据权限")
    private Integer dataScope;

    @Schema(description = "状态")
    private StatusEnum status;
}