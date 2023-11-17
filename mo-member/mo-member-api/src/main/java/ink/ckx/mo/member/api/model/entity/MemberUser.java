package ink.ckx.mo.member.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import ink.ckx.mo.common.core.base.BaseEntity;
import ink.ckx.mo.common.web.enums.GenderEnum;
import ink.ckx.mo.common.web.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberUser extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String mobile;

    private String nickName;

    private GenderEnum gender;

    private LocalDate birthday;

    private String avatarUrl;

    private String openid;

    private StatusEnum status;
}