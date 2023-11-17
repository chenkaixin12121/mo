package ink.ckx.mo.system.api.model.query;

import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 部门列表Query
 *
 * @author chenkaixin
 */
@Schema(description = "部门列表Query")
@Data
public class DeptListQuery {

    @Schema(description = "关键字(部门名称)")
    private String keywords;

    @Schema(description = "状态")
    private StatusEnum status;
}