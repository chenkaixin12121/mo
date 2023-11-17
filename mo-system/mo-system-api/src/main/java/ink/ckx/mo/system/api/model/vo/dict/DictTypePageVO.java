package ink.ckx.mo.system.api.model.vo.dict;

import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "字典类型分页VO")
@Data
public class DictTypePageVO {

    @Schema(description = "字典类型ID")
    private Long id;

    @Schema(description = "类型名称")
    private String name;

    @Schema(description = "类型编码")
    private String code;

    @Schema(description = "类型状态")
    private StatusEnum status;
}