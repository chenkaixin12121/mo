package ink.ckx.mo.system.api.model.form;

import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "部门表单对象")
@Data
public class DeptForm {

    @NotBlank(message = "部门名称不能为空")
    @Schema(description = "部门名称")
    private String name;

    @NotNull(message = "父部门ID不能为空")
    @Schema(description = "父部门ID")
    private Long parentId;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态")
    private StatusEnum status;

    @NotNull(message = "排序不能为空")
    @Schema(description = "排序")
    private Integer sort;
}