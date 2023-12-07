package ink.ckx.mo.common.oss.query

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
@Schema(description = "删除文件Query")
data class DeleteFileQuery(

    @field:NotBlank(message = "key不能为空")
    @Schema(description = "key")
    var key: String? = null
)