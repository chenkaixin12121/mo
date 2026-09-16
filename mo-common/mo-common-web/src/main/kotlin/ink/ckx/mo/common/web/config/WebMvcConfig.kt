package ink.ckx.mo.common.web.config

import cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN
import cn.hutool.core.date.DatePattern.NORM_DATE_PATTERN
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import ink.ckx.mo.common.core.base.IBaseEnum
import ink.ckx.mo.common.web.converter.IBaseEnumDeserializer
import ink.ckx.mo.common.web.converter.StatusEnumConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * @author chenkaixin
 * @description
 * @since 2023/11/18
 */
@Configuration
class WebMvcConfig : WebMvcConfigurer {

    @Bean
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        //序列化的时候序列对象的所有非空属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        // 反序列化时候遇到不匹配的属性并不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        // 序列化时候遇到空对象不抛出异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        // 反序列化的时候如果是无效子类型,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
        // 不使用默认的dateTime进行序列化,
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false)
        val module = SimpleModule()
        // 时间格式化
        module.addSerializer(LocalDate::class.java, LocalDateSerializer(DateTimeFormatter.ofPattern(NORM_DATE_PATTERN)))
        module.addSerializer(
            LocalDateTime::class.java,
            LocalDateTimeSerializer(DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN))
        )
        module.addDeserializer(
            LocalDate::class.java,
            LocalDateDeserializer(DateTimeFormatter.ofPattern(NORM_DATE_PATTERN))
        )
        module.addDeserializer(
            LocalDateTime::class.java,
            LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN))
        )
        // js精度丢失
        module.addSerializer(Long::class.javaPrimitiveType, ToStringSerializer.instance)
        module.addSerializer(Long::class.javaObjectType, ToStringSerializer.instance)

        // 枚举反序列化：前端传空字符串时解析为 null，避免解析参数失败
        val enumModule = SimpleModule()
        enumModule.setDeserializers(object : SimpleDeserializers() {
            override fun findEnumDeserializer(
                type: Class<*>,
                config: DeserializationConfig,
                beanDesc: BeanDescription
            ): JsonDeserializer<*>? {
                return if (IBaseEnum::class.java.isAssignableFrom(type)) {
                    @Suppress("UNCHECKED_CAST")
                    IBaseEnumDeserializer(type as Class<out Enum<*>>)
                } else {
                    super.findEnumDeserializer(type, config, beanDesc)
                }
            }
        })

        objectMapper.registerModules(JavaTimeModule(), module, enumModule)
        return objectMapper
    }

    override fun addFormatters(registry: FormatterRegistry) {
        // 枚举参数绑定：前端传空字符串时转换为 null，避免解析参数失败
        // 注意：Spring 默认的 StringToEnumConverterFactory 优先级较高，需针对具体枚举类型注册精确 Converter 才能生效
        registry.addConverter(StatusEnumConverter())
    }
}