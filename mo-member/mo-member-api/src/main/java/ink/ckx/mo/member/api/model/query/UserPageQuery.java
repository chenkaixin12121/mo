package ink.ckx.mo.member.api.model.query;

import ink.ckx.mo.common.core.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/18
 */
@Schema(description = "会员用户分页Query")
@Data
public class UserPageQuery extends BasePageQuery {

    @Schema(description = "关键字")
    private String keywords;
}
