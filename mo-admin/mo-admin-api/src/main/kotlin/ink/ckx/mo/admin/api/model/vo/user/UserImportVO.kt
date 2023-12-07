package ink.ckx.mo.admin.api.model.vo.user

import com.alibaba.excel.annotation.ExcelProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/23
 */
@Schema(description = "用户导入VO")
data class UserImportVO(

    @Schema(description = "用户名")
    @ExcelProperty(value = ["用户名"])
    var username: String? = null,

    @Schema(description = "用户昵称")
    @ExcelProperty(value = ["用户昵称"])
    var nickname: String? = null,

    @Schema(description = "性别")
    @ExcelProperty(value = ["性别"])
    var gender: String? = null,

    @Schema(description = "手机号码")
    @ExcelProperty(value = ["手机号码"])
    var mobile: String? = null,

    @Schema(description = "邮箱")
    @ExcelProperty(value = ["邮箱"])
    var email: String? = null,
)