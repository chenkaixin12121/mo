package ink.ckx.mo.system.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import ink.ckx.mo.common.core.base.BaseEntity;
import ink.ckx.mo.common.web.enums.GenderEnum;
import ink.ckx.mo.common.web.enums.StatusEnum;
import lombok.Data;

@Data
public class SysUser extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String username;

    private String nickname;

    private GenderEnum gender;

    private String password;

    private Long deptId;

    private String avatar;

    private String mobile;

    private String email;

    private StatusEnum status;
}