package ink.ckx.mo.system.api.model.query;

import ink.ckx.mo.common.core.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "字典类型分页查询对象")
@Data
public class DictTypePageQuery extends BasePageQuery {

    @Schema(description = "关键字(类型名称/类型编码)")
    private String keywords;
}