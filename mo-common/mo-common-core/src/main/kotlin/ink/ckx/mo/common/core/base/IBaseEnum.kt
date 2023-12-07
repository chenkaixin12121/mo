package ink.ckx.mo.common.core.base

import cn.hutool.core.util.ObjectUtil
import java.util.*

/**
 * 枚举通用接口
 *
 * @author chenkaixin
 */
interface IBaseEnum<T> {

    val value: T
    val label: String

    companion object {
        /**
         * 根据label获取枚举
         *
         * @param label
         * @param clazz
         * @param <E>   枚举
         * @return </E>
         */
        @JvmStatic
        fun <E> getEnumByLabel(label: Any, clazz: Class<E>): E where E : Enum<E>, E : IBaseEnum<*> {
            Objects.requireNonNull(label)
            val allEnums = EnumSet.allOf(clazz) // 获取类型下的所有枚举
            return allEnums.first { ObjectUtil.equal(it.label, label) }
        }

        /**
         * 根据值获取枚举
         *
         * @param value
         * @param clazz
         * @param <E>   枚举
         * @return </E>
         */
        @JvmStatic
        fun <E> getEnumByValue(value: Any, clazz: Class<E>): E where E : Enum<E>, E : IBaseEnum<*> {
            Objects.requireNonNull(value)
            val allEnums = EnumSet.allOf(clazz) // 获取类型下的所有枚举
            return allEnums.first { ObjectUtil.equal(it.value, value) }
        }

        /**
         * 根据文本标签获取值
         *
         * @param value
         * @param clazz
         * @param <E>
         * @return </E>
         */
        fun <E> getLabelByValue(value: Any, clazz: Class<E>): String where E : Enum<E>, E : IBaseEnum<*> {
            Objects.requireNonNull(value)
            val allEnums = EnumSet.allOf(clazz) // 获取类型下的所有枚举
            val matchEnum = allEnums.first { ObjectUtil.equal(it.value, value) }
            return matchEnum?.label.toString()
        }

        /**
         * 根据文本标签获取值
         *
         * @param label
         * @param clazz
         * @param <E>
         * @return </E>
         */
        fun <E> getValueByLabel(label: String, clazz: Class<E>): Any where E : Enum<E>, E : IBaseEnum<*> {
            Objects.requireNonNull(label)
            val allEnums = EnumSet.allOf(clazz) // 获取类型下的所有枚举
            val matchEnum = allEnums.first { ObjectUtil.equal(it.label, label) }
            return matchEnum?.label.toString()
        }
    }
}