package ink.ckx.mo.system.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import ink.ckx.mo.common.core.base.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单类型枚举
 *
 * @author chenkaixin
 */
@AllArgsConstructor
@Getter
public enum MenuTypeEnum implements IBaseEnum<Integer> {

    NULL(0, null),
    CATALOG(1, "目录"),
    MENU(2, "菜单"),
    BUTTON(3, "按钮");

    @JsonValue
    @EnumValue
    private final Integer value;

    private final String label;
}