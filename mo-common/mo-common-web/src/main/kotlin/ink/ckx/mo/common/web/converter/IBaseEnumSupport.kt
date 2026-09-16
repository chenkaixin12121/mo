package ink.ckx.mo.common.web.converter

import ink.ckx.mo.common.core.base.IBaseEnum

/**
 * 枚举匹配工具
 *
 * @author chenkaixin
 */
internal object IBaseEnumSupport {

    /**
     * 根据值或名称匹配枚举
     * <p>
     * 优先按枚举 value 匹配，其次按 value 的字符串形式匹配，最后按枚举名称匹配。
     * 值为空、空白或匹配不到时返回 null。
     *
     * @param value     待匹配的值（字符串或数值）
     * @param enumClass 枚举类
     * @return 匹配到的枚举，匹配不到时返回 null
     */
    fun match(value: Any?, enumClass: Class<out Enum<*>>): Enum<*>? {
        if (value == null) return null
        val text = value.toString()
        if (text.isBlank()) return null
        val allEnums = enumClass.enumConstants?.toList() ?: return null
        return allEnums.firstOrNull { (it as IBaseEnum<*>).value == value }
            ?: allEnums.firstOrNull { (it as IBaseEnum<*>).value?.toString() == text }
            ?: allEnums.firstOrNull { it.name == text }
    }
}
