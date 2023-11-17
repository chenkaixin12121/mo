package ink.ckx.mo.common.log.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/17
 */
@Schema(description = "操作日志封装")
@Data
@EqualsAndHashCode(callSuper = false)
public class WebLog {

    @Schema(description = "操作描述")
    private String description;

    @Schema(description = "操作用户")
    private String username;

    @Schema(description = "操作时间")
    private Long startTime;

    @Schema(description = "消耗时间")
    private Integer spendTime;

    @Schema(description = "根路径")
    private String basePath;

    @Schema(description = "URI")
    private String uri;

    @Schema(description = "URL")
    private String url;

    @Schema(description = "请求类型")
    private String method;

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "请求参数")
    private Object parameter;

    @Schema(description = "返回结果")
    private Object result;
}