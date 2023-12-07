package ink.ckx.mo.common.web.model

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 下拉选项对象
 *
 * @author chenkaixin
 */
@Schema(description = "下拉选项对象")
class Option<T>(

    @Schema(description = "选项的值")
    var value: T?,

    @Schema(description = "选项的标签")
    var label: String?,

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @Schema(description = "子选项列表")
    var children: List<Option<T>>?
) {

    constructor() : this(null, null, null)

    constructor(value: T?, label: String?) : this(value, label, null)
}