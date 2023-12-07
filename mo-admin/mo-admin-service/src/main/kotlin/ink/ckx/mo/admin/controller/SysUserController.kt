package ink.ckx.mo.admin.controller

import com.alibaba.excel.EasyExcel
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateChainWrapper
import ink.ckx.mo.admin.api.model.dto.SysUserInfoDTO
import ink.ckx.mo.admin.api.model.entity.SysUser
import ink.ckx.mo.admin.api.model.form.UserForm
import ink.ckx.mo.admin.api.model.query.*
import ink.ckx.mo.admin.api.model.vo.user.*
import ink.ckx.mo.admin.listener.UserImportListener
import ink.ckx.mo.admin.service.SysUserService
import ink.ckx.mo.common.core.result.Result
import ink.ckx.mo.common.core.result.Result.Companion.success
import ink.ckx.mo.common.mybatis.result.PageResult
import ink.ckx.mo.common.security.inner.Inner
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * 用户控制器
 *
 * @author chenkaixin
 */
@Validated
@Tag(name = "用户接口")
@RestController
@RequestMapping("/api/v1/users")
class SysUserController(
    private val userService: SysUserService
) {

    @Inner
    @Operation(summary = "用户认证信息")
    @GetMapping("/{username}/authInfo")
    fun getUserAuthInfo(@Parameter(description = "用户名") @PathVariable username: String): Result<SysUserInfoDTO?> {
        val sysUserInfoDTO = userService.getUserAuthInfo(username)
        return success(sysUserInfoDTO)
    }

    @Operation(summary = "用户分页列表")
    @PostMapping("/pages")
    fun listUserPages(@RequestBody queryParams: UserPageQuery): PageResult<UserPageVO> {
        val result = userService.listUserPages(queryParams)
        return PageResult.success(result)
    }

    @Operation(summary = "用户详情")
    @GetMapping("/{userId}")
    fun getUserDetail(@Parameter(description = "用户ID") @PathVariable userId: Long): Result<UserDetailVO?> {
        val userDetailVO = userService.getUserDetail(userId)
        return success(userDetailVO)
    }

    @PreAuthorize("@pms.hasPerm('sys:user:save')")
    @Operation(summary = "新增用户")
    @PostMapping
    fun saveUser(@RequestBody @Valid userForm: UserForm): Result<Long?> {
        val userId = userService.saveUser(userForm)
        return success(userId)
    }

    @PreAuthorize("@pms.hasPerm('sys:user:update')")
    @Operation(summary = "修改用户")
    @PutMapping(value = ["/{userId}"])
    fun updateUser(
        @Parameter(description = "用户ID") @PathVariable userId: Long,
        @RequestBody @Validated userForm: UserForm
    ): Result<Void?> {
        userService.updateUser(userId, userForm)
        return success()
    }

    @PreAuthorize("@pms.hasPerm('sys:user:delete')")
    @Operation(summary = "删除用户")
    @DeleteMapping("/{ids}")
    fun deleteUsers(@Parameter(description = "用户ID，多个以英文逗号(,)分割") @PathVariable ids: String): Result<Void?> {
        userService.deleteUsers(ids)
        return success()
    }

    @PreAuthorize("@pms.hasPerm('sys:user:update:password')")
    @Operation(summary = "修改用户密码")
    @PatchMapping(value = ["/{userId}/password"])
    fun updatePassword(
        @Parameter(description = "用户ID") @PathVariable userId: Long,
        @Parameter(description = "密码") @RequestParam password: String
    ): Result<Void?> {
        userService.updatePassword(userId, password)
        return success()
    }

    @PreAuthorize("@pms.hasPerm('sys:user:update')")
    @Operation(summary = "修改用户状态")
    @PatchMapping(value = ["/{userId}/status"])
    fun updateUserPassword(
        @Parameter(description = "用户ID") @PathVariable userId: Long,
        @Parameter(description = "用户状态") @RequestParam status: Int,
    ): Result<Void?> {
        KtUpdateChainWrapper(SysUser())
            .eq(SysUser::id, userId)
            .set(SysUser::status, status)
            .update()
        return success()
    }

    @GetMapping("/info")
    @Operation(summary = "获取登录用户信息")
    fun loginUserInfo(): Result<UserLoginVO> {
        val userLoginVO = userService.loginUserInfo()
        return success(userLoginVO)
    }

    @Operation(summary = "登出")
    @PostMapping("/logout")
    fun logout(): Result<Void?> {
        userService.logout()
        return success()
    }

    @Operation(summary = "用户导入模板下载")
    @GetMapping("/template")
    fun downloadTemplate(response: HttpServletResponse) {
        val fileName = "用户导入模板.xlsx"
        response.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        response.setHeader(
            "Content-Disposition",
            "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8)
        )
        val fileClassPath = "excel-templates" + File.separator + fileName
        val inputStream = this.javaClass.classLoader.getResourceAsStream(fileClassPath)
        val outputStream = response.outputStream
        val excelWriter = EasyExcel.write(outputStream).withTemplate(inputStream).build()
        excelWriter.finish()
    }

    @PreAuthorize("@pms.hasPerm('sys:user:_import')")
    @Operation(summary = "导入用户")
    @PostMapping("/_import")
    fun importUsers(
        @Parameter(description = "部门ID") deptId: Long,
        @Parameter(description = "角色ID") roleIds: String,
        file: MultipartFile
    ): Result<String?> {
        val listener = UserImportListener(deptId, roleIds)
        EasyExcel.read(file.inputStream, UserImportVO::class.java, listener).sheet().doRead()
        val msg = listener.msg.toString()
        return success(msg)
    }

    @PreAuthorize("@pms.hasPerm('sys:user:_export')")
    @Operation(summary = "导出用户")
    @GetMapping("/_export")
    fun exportUsers(queryParams: UserPageQuery, response: HttpServletResponse) {
        val fileName = "用户列表.xlsx"
        response.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        response.setHeader(
            "Content-Disposition",
            "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8)
        )
        val exportUserList = userService.listExportUsers(queryParams)
        EasyExcel.write(response.outputStream, UserExportVO::class.java).sheet("用户列表")
            .doWrite(exportUserList)
    }
}