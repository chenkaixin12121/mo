package ink.ckx.mo.common.mybatis.enums

import com.baomidou.mybatisplus.annotation.EnumValue
import com.fasterxml.jackson.annotation.JsonValue
import ink.ckx.mo.common.core.base.IBaseEnum

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/21
 */
enum class DataScopeEnum(
    @JsonValue
    @EnumValue
    override var value: Int,
    override var label: String
) : IBaseEnum<Int> {

    ALL(0, "所有数据"),
    DEPT_AND_SUB(10, "部门及子部门数据"),
    DEPT(20, "本部门数据"),
    SELF(30, "本人数据");
}