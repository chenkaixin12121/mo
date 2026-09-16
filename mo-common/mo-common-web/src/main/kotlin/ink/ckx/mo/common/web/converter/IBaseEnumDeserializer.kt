package ink.ckx.mo.common.web.converter

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

/**
 * 枚举反序列化器
 * <p>
 * 前端传空字符串时解析为 null，避免 Jackson 反序列化参数失败。
 *
 * @author chenkaixin
 */
class IBaseEnumDeserializer(
    private val enumClass: Class<out Enum<*>>
) : JsonDeserializer<Enum<*>>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Enum<*>? {
        val value: Any? = when (p.currentToken) {
            JsonToken.VALUE_STRING -> p.text
            JsonToken.VALUE_NUMBER_INT, JsonToken.VALUE_NUMBER_FLOAT -> p.numberValue
            else -> null
        }
        // 空值/空字符串直接返回 null，非空但匹配不到则抛出异常
        if (value == null || value.toString().isBlank()) return null
        return IBaseEnumSupport.match(value, enumClass)
            ?: throw IllegalArgumentException("无法识别的枚举值: $value")
    }
}
