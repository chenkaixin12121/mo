package ink.ckx.mo.common.web.enums

import com.baomidou.mybatisplus.annotation.EnumValue
import com.fasterxml.jackson.annotation.JsonValue
import ink.ckx.mo.common.core.base.IBaseEnum

/**
 * 状态枚举
 *
 * @author chenkaixin
 */
enum class StatusEnum(

    @JsonValue
    @EnumValue
    override var value: Int,

    override var label: String,
) : IBaseEnum<Int> {

    DISABLE(0, "禁用"),
    ENABLE(1, "启用"),
    ;
}