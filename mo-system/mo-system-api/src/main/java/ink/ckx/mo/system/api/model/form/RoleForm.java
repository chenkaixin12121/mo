package ink.ckx.mo.system.api.model.form;

import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "角色表单对象")
@Data
public class RoleForm {

    @NotBlank(message = "角色名称不能为空")
    @Schema(description = "角色名称")
    private String name;

    @NotBlank(message = "角色编码不能为空")
    @Schema(description = "角色编码")
    private String code;

    @NotNull(message = "排序不能为空")
    @Schema(description = "排序")
    private Integer sort;

    @NotNull(message = "数据权限不能为空")
    @Schema(description = "数据权限")
    private Integer dataScope;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态")
    private StatusEnum status;
}