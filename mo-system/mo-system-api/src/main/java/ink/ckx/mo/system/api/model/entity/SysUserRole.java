package ink.ckx.mo.system.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysUserRole {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private Long roleId;
}