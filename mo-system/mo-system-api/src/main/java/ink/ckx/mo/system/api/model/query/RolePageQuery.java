package ink.ckx.mo.system.api.model.query;

import ink.ckx.mo.common.core.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色分页Query
 *
 * @author chenkaixin
 */
@Data
public class RolePageQuery extends BasePageQuery {

    @Schema(description = "关键字(角色名称/角色编码)")
    private String keywords;
}