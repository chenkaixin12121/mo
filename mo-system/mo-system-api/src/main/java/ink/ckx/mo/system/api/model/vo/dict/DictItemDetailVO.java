package ink.ckx.mo.system.api.model.vo.dict;

import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "字典数据项详情VO")
@Data
public class DictItemDetailVO {

    @Schema(description = "数据项ID")
    private Long id;

    @Schema(description = "类型编码")
    private String typeCode;

    @Schema(description = "数据项名称")
    private String name;

    @Schema(description = "值")
    private String value;

    @Schema(description = "状态")
    private StatusEnum status;

    @Schema(description = "排序")
    private Integer sort;
}