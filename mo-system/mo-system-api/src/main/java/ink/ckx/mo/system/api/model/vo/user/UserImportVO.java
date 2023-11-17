package ink.ckx.mo.system.api.model.vo.user;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/23
 */
@Schema(description = "用户导入VO")
@Data
public class UserImportVO {

    @Schema(description = "用户名")
    @ExcelProperty(value = "用户名")
    private String username;

    @Schema(description = "用户昵称")
    @ExcelProperty(value = "用户昵称")
    private String nickname;

    @Schema(description = "性别")
    @ExcelProperty(value = "性别")
    private String gender;

    @Schema(description = "手机号码")
    @ExcelProperty(value = "手机号码")
    private String mobile;

    @Schema(description = "邮箱")
    @ExcelProperty(value = "邮箱")
    private String email;
}