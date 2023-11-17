package ink.ckx.mo.system.api.model.vo.dept;

import com.fasterxml.jackson.annotation.JsonFormat;
import ink.ckx.mo.common.web.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "部门列表VO")
@Data
public class DeptListVO {

    @Schema(description = "部门id")
    private Long id;

    @Schema(description = "父部门id")
    private Long parentId;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态")
    private StatusEnum status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "子部门")
    private List<DeptListVO> children;
}