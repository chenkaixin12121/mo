package ink.ckx.mo.common.web.converter

import ink.ckx.mo.common.web.enums.StatusEnum
import org.springframework.core.convert.converter.Converter

/**
 * 状态枚举字符串转换器
 * <p>
 * 处理 @RequestParam、@ModelAttribute 绑定参数时前端传空字符串的解析，空值转换为 null。
 *
 * @author chenkaixin
 */
class StatusEnumConverter : Converter<String, StatusEnum> {

    override fun convert(source: String): StatusEnum? {
        // 空字符串/空白转换为 null
        if (source.isBlank()) return null
        return IBaseEnumSupport.match(source, StatusEnum::class.java) as? StatusEnum
            ?: throw IllegalArgumentException("无法识别的枚举值: $source")
    }
}
