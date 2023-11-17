package ink.ckx.mo.system.api.model.vo.menu;

import com.fasterxml.jackson.annotation.JsonFormat;
import ink.ckx.mo.common.web.enums.StatusEnum;
import ink.ckx.mo.system.api.enums.MenuTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "菜单列表VO")
@Data
public class MenuListVO {

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

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "权限标识")
    private String perm;

    @Schema(description = "状态")
    private StatusEnum visible;

    @Schema(description = "菜单类型")
    private MenuTypeEnum type;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "子节点")
    private List<MenuListVO> children;
}