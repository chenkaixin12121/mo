package ink.ckx.mo.system.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import ink.ckx.mo.common.core.base.BaseEntity;
import ink.ckx.mo.common.web.enums.StatusEnum;
import lombok.Data;

@Data
public class SysRole extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    private String code;

    private Integer sort;

    private Integer dataScope;

    private StatusEnum status;
}