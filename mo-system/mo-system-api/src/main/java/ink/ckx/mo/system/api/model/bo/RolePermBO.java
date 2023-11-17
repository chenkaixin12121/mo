package ink.ckx.mo.system.api.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/16
 */
@Schema(description = "角色权限BO")
@Data
public class RolePermBO {

    @Schema(description = "权限标识")
    private String perm;

    @Schema(description = "角色编码")
    private String code;
}
