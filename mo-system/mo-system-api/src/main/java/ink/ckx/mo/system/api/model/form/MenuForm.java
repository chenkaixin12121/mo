package ink.ckx.mo.system.api.model.form;

import ink.ckx.mo.common.web.enums.StatusEnum;
import ink.ckx.mo.system.api.enums.MenuTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author chenkaixin
 * @description TODO
 * @since 2023/11/15
 */
@Schema(description = "菜单表单")
@Data
public class MenuForm {

    @Schema(description = "父级id")
    private Long parentId;

    @NotNull(message = "菜单名称不能为空")
    @Schema(description = "名称")
    private String name;

    @NotNull(message = "菜单类型不能为空")
    @Schema(description = "菜单类型")
    private MenuTypeEnum type;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "始终显示")
    private Integer alwaysShow;

    @Schema(description = "路由路径")
    private String path;

    @Schema(description = "权限标识")
    private String perm;

    @Schema(description = "组件路径")
    private String component;

    @NotNull(message = "排序不能为空")
    @Schema(description = "排序")
    private Integer sort;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态")
    private StatusEnum visible;
}