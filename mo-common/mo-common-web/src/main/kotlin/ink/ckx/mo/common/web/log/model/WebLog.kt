package ink.ckx.mo.common.web.log.model

import io.swagger.v3.oas.annotations.media.Schema

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/17
 */
@Schema(description = "操作日志封装")
data class WebLog(

    @Schema(description = "操作描述")
    var description: String? = null,

    @Schema(description = "操作用户")
    var username: String? = null,

    @Schema(description = "操作时间")
    var startTime: Int? = null,

    @Schema(description = "消耗时间")
    var spendTime: Int? = null,

    @Schema(description = "根路径")
    var basePath: String? = null,

    @Schema(description = "URI")
    var uri: String? = null,

    @Schema(description = "URL")
    var url: String? = null,

    @Schema(description = "请求类型")
    var method: String? = null,

    @Schema(description = "IP地址")
    var ip: String? = null,

    @Schema(description = "请求参数")
    var parameter: Any? = null,

    @Schema(description = "返回结果")
    var result: Any? = null,
) 