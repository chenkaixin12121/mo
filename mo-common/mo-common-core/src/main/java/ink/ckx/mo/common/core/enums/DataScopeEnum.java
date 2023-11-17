package ink.ckx.mo.common.core.enums;

import ink.ckx.mo.common.core.base.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/21
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum DataScopeEnum implements IBaseEnum<Integer> {

    ALL(0, "所有数据"),
    DEPT_AND_SUB(10, "部门及子部门数据"),
    DEPT(20, "本部门数据"),
    SELF(30, "本人数据");

    private Integer value;

    private String label;
}
