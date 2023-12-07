package ink.ckx.mo.common.web.enums

import com.baomidou.mybatisplus.annotation.EnumValue
import com.fasterxml.jackson.annotation.JsonValue
import ink.ckx.mo.common.core.base.IBaseEnum

/**
 * 性别枚举
 *
 * @author chenkaixin
 */
enum class GenderEnum(

    @JsonValue
    @EnumValue
    override var value: Int,

    override var label: String,
) : IBaseEnum<Int> {

    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女"),
    ;
}