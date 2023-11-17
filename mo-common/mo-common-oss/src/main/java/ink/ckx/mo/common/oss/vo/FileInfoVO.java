package ink.ckx.mo.common.oss.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
@Schema(description = "文件信息VO")
@Data
public class FileInfoVO {

    @Schema(description = "文件key")
    private String key;

    @Schema(description = "访问url")
    private String accessUrl;
}