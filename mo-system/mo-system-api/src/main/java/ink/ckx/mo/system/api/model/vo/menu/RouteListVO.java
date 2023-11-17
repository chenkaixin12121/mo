package ink.ckx.mo.system.api.model.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 路由列表VO
 *
 * @author chenkaixin
 */
@Schema(description = "路由列表VO")
@Data
public class RouteListVO {

    @Schema(description = "路由路径")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "路由路径")
    private String name;

    @Schema(description = "元数据")
    private Meta meta;

    @Schema(description = "子节点")
    private List<RouteListVO> children;

    @Schema(description = "元数据")
    @Data
    public static class Meta {

        @Schema(description = "标题")
        private String title;

        @Schema(description = "图标")
        private String icon;

        @Schema(description = "隐藏")
        private Boolean hidden;

        @Schema(description = "如果设置为 true，目录没有子节点也会显示")
        private Boolean alwaysShow;

        @Schema(description = "角色")
        private List<String> roles;

        @Schema(description = "页面缓存开启状态")
        private Boolean keepAlive;
    }
}
