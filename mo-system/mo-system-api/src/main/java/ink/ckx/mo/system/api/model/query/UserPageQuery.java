package ink.ckx.mo.system.api.model.query;

import ink.ckx.mo.common.core.base.BasePageQuery;
import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户分页Query
 *
 * @author chenkaixin
 */
@Schema(description = "用户分页Query")
@Data
public class UserPageQuery extends BasePageQuery {

    @Schema(description = "关键字(用户名/昵称/手机号)")
    private String keywords;

    @Schema(description = "用户状态")
    private StatusEnum status;

    @Schema(description = "部门ID")
    private Long deptId;
}