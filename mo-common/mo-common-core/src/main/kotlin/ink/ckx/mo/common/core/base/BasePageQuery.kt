package ink.ckx.mo.common.core.base

import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

/**
 * @author chenkaixin
 * @desc 基础分页请求对象
 */
@Schema(description = "基础分页对象")
open class BasePageQuery : Serializable {

    @Schema(description = "页码", example = "1")
    val pageNum: Long = 1

    @Schema(description = "每页记录数", example = "10")
    val pageSize: Long = 10
}