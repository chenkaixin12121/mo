package ink.ckx.mo.common.oss.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
@Data
@Schema(description = "删除文件Query")
public class DeleteFileQuery {

    @NotBlank(message = "key不能为空")
    @Schema(description = "key")
    private String key;
}