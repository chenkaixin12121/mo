package ink.ckx.mo.system.api.model.form;

import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "字典数据项")
@Data
public class DictItemForm {

    @NotBlank(message = "类型编码不能为空")
    @Schema(description = "类型编码")
    private String typeCode;

    @NotBlank(message = "数据项名称不能为空")
    @Schema(description = "数据项名称")
    private String name;

    @NotBlank(message = "值不能为空")
    @Schema(description = "值")
    private String value;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态")
    private StatusEnum status;

    @NotNull(message = "排序不能为空")
    @Schema(description = "排序")
    private Integer sort;
}