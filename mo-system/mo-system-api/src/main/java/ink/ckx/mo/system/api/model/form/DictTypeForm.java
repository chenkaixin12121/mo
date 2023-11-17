package ink.ckx.mo.system.api.model.form;

import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "字典类型")
@Data
public class DictTypeForm {

    @NotBlank(message = "类型名称不能为空")
    @Schema(description = "类型名称")
    private String name;

    @NotBlank(message = "类型编码不能为空")
    @Schema(description = "类型编码")
    private String code;

    @NotNull(message = "类型状态不能为空")
    @Schema(description = "类型状态")
    private StatusEnum status;

    @Schema(description = "备注")
    private String remark;
}