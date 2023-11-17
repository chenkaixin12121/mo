package ink.ckx.mo.system.api.model.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "资源列表VO")
@Data
public class ResourceListVO {

    @Schema(description = "选项的值")
    private Long value;

    @Schema(description = "选项的标签")
    private String label;

    @Schema(description = "子菜单")
    private List<ResourceListVO> children;
}