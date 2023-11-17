package ink.ckx.mo.system.api.model.vo.dict;

import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "字典数据项分页VO")
@Data
public class DictItemPageVO {

    @Schema(description = "数据项ID")
    private Long id;

    @Schema(description = "数据项名称")
    private String name;

    @Schema(description = "值")
    private String value;

    @Schema(description = "类型状态")
    private StatusEnum status;
}