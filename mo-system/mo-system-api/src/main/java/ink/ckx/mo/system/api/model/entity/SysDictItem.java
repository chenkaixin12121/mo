package ink.ckx.mo.system.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import ink.ckx.mo.common.core.base.BaseEntity;
import ink.ckx.mo.common.web.enums.StatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SysDictItem extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String typeCode;

    private String name;

    private String value;

    private StatusEnum status;

    private Integer sort;

    private Boolean defaulted;

    private String remark;
}