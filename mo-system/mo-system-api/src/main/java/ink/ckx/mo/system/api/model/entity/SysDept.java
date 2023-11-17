package ink.ckx.mo.system.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import ink.ckx.mo.common.core.base.BaseEntity;
import ink.ckx.mo.common.web.enums.StatusEnum;
import lombok.Data;

@Data
public class SysDept extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    private Long parentId;

    private String treePath;

    private Integer sort;

    private StatusEnum status;

    private Integer deleted;
}