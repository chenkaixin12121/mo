package ink.ckx.mo.admin.api.enums

import com.baomidou.mybatisplus.annotation.EnumValue
import com.fasterxml.jackson.annotation.JsonValue
import ink.ckx.mo.common.core.base.IBaseEnum

/**
 * 菜单类型枚举
 *
 * @author chenkaixin
 */
enum class MenuTypeEnum(

    @JsonValue
    @EnumValue
    override var value: Int,
    override var label: String
) : IBaseEnum<Int> {

    CATALOG(1, "目录"),
    MENU(2, "菜单"),
    BUTTON(3, "按钮"),
    ;
}