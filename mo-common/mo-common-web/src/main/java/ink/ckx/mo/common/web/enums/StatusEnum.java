package ink.ckx.mo.common.web.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import ink.ckx.mo.common.core.base.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态枚举
 *
 * @author chenkaixin
 */
@AllArgsConstructor
@Getter
public enum StatusEnum implements IBaseEnum<Integer> {

    DISABLE(0, "禁用"),
    ENABLE(1, "启用"),
    ;

    @JsonValue
    @EnumValue
    private final Integer value;

    private final String label;
}