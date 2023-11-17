package ink.ckx.mo.system.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import ink.ckx.mo.common.core.base.BaseEntity;
import ink.ckx.mo.common.web.enums.StatusEnum;
import ink.ckx.mo.system.api.enums.MenuTypeEnum;
import lombok.Data;

/**
 * 菜单实体类
 *
 * @author chenkaixin
 */
@Data
public class SysMenu extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long parentId;

    private String treePath;

    private String name;

    private MenuTypeEnum type;

    private String path;

    private String component;

    private String perm;

    private String icon;

    private Integer sort;

    private Integer alwaysShow;

    private StatusEnum visible;
}