package ink.ckx.mo.common.oss.vo

import io.swagger.v3.oas.annotations.media.Schema

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
@Schema(description = "文件信息VO")
data class FileInfoVO(

    @Schema(description = "文件key")
    var key: String? = null,

    @Schema(description = "访问url")
    var accessUrl: String? = null,
)