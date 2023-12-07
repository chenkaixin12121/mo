package ink.ckx.mo.admin.api.model.vo.user

import com.alibaba.excel.annotation.ExcelProperty
import com.alibaba.excel.annotation.format.DateTimeFormat
import com.alibaba.excel.annotation.write.style.ColumnWidth
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/23
 */
@Schema(description = "用户导出VO")
@ColumnWidth(20)
data class UserExportVO(

    @Schema(description = "用户名")
    @ExcelProperty(value = ["用户名"])
    var username: String? = null,

    @Schema(description = "用户昵称")
    @ExcelProperty(value = ["用户昵称"])
    var nickname: String? = null,

    @Schema(description = "部门")
    @ExcelProperty(value = ["部门"])
    var deptName: String? = null,

    @Schema(description = "性别")
    @ExcelProperty(value = ["性别"])
    var gender: String? = null,

    @Schema(description = "手机号码")
    @ExcelProperty(value = ["手机号码"])
    var mobile: String? = null,

    @Schema(description = "邮箱")
    @ExcelProperty(value = ["邮箱"])
    var email: String? = null,

    @DateTimeFormat("yyyy/MM/dd HH:mm:ss")
    @Schema(description = "创建时间")
    @ExcelProperty(value = ["创建时间"])
    var createTime: LocalDateTime? = null,
)