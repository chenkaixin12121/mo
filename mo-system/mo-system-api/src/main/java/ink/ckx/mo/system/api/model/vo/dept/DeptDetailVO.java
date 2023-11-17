package ink.ckx.mo.system.api.model.vo.dept;

import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "部门详情VO")
@Data
public class DeptDetailVO {

    @Schema(description = "部门ID")
    private Long id;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "父部门ID")
    private Long parentId;

    @Schema(description = "状态")
    private StatusEnum status;

    @Schema(description = "排序")
    private Integer sort;
}