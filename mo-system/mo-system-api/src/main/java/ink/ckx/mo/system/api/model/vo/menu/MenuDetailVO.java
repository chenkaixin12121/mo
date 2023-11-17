package ink.ckx.mo.system.api.model.vo.menu;

import ink.ckx.mo.common.web.enums.StatusEnum;
import ink.ckx.mo.system.api.enums.MenuTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "菜单详情VO")
@Data
public class MenuDetailVO {

    @Schema(description = "菜单id")
    private Long id;

    @Schema(description = "父级id")
    private Long parentId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "路由路径")
    private String path;

    @Schema(description = "权限标识")
    private String perm;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态")
    private StatusEnum visible;

    @Schema(description = "类型")
    private MenuTypeEnum type;
}