package ink.ckx.mo.common.web.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import ink.ckx.mo.common.core.base.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举
 *
 * @author chenkaixin
 */
@AllArgsConstructor
@Getter
public enum GenderEnum implements IBaseEnum<Integer> {

    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女"),
    ;

    @JsonValue
    @EnumValue
    private final Integer value;

    private final String label;
}