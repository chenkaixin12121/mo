package ink.ckx.mo.system.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import ink.ckx.mo.common.core.result.Result;
import ink.ckx.mo.common.mybatis.result.PageResult;
import ink.ckx.mo.common.security.inner.Inner;
import ink.ckx.mo.system.api.model.dto.SysUserInfoDTO;
import ink.ckx.mo.system.api.model.entity.SysUser;
import ink.ckx.mo.system.api.model.form.UserForm;
import ink.ckx.mo.system.api.model.query.UserPageQuery;
import ink.ckx.mo.system.api.model.vo.user.*;
import ink.ckx.mo.system.listener.UserImportListener;
import ink.ckx.mo.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 用户控制器
 *
 * @author chenkaixin
 */
@Validated
@Tag(name = "用户接口")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    @Inner
    @Operation(summary = "用户认证信息")
    @GetMapping("/{username}/authInfo")
    public Result<SysUserInfoDTO> getUserAuthInfo(@Parameter(description = "用户名") @PathVariable String username) {
        SysUserInfoDTO sysUserInfoDTO = userService.getUserAuthInfo(username);
        return Result.success(sysUserInfoDTO);
    }

    @Operation(summary = "用户分页列表")
    @PostMapping("/pages")
    public PageResult<UserPageVO> listUserPages(@RequestBody UserPageQuery queryParams) {
        IPage<UserPageVO> result = userService.listUserPages(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "用户详情")
    @GetMapping("/{userId}")
    public Result<UserDetailVO> getUserDetail(@Parameter(description = "用户ID") @PathVariable Long userId) {
        UserDetailVO userDetailVO = userService.getUserDetail(userId);
        return Result.success(userDetailVO);
    }

    @PreAuthorize("@pms.hasPerm('sys:user:add')")
    @Operation(summary = "新增用户")
    @PostMapping
    public Result<Long> saveUser(@RequestBody @Valid UserForm userForm) {
        Long userId = userService.saveUser(userForm);
        return Result.success(userId);
    }

    @PreAuthorize("@pms.hasPerm('sys:user:update')")
    @Operation(summary = "修改用户")
    @PutMapping(value = "/{userId}")
    public Result<Void> updateUser(@Parameter(description = "用户ID") @PathVariable Long userId,
                                   @RequestBody @Validated UserForm userForm) {
        userService.updateUser(userId, userForm);
        return Result.success();
    }

    @PreAuthorize("@pms.hasPerm('sys:user:delete')")
    @Operation(summary = "删除用户")
    @DeleteMapping("/{ids}")
    public Result<Void> deleteUsers(@Parameter(description = "用户ID，多个以英文逗号(,)分割") @PathVariable String ids) {
        userService.deleteUsers(ids);
        return Result.success();
    }

    @PreAuthorize("@pms.hasPerm('sys:user:update:password')")
    @Operation(summary = "修改用户密码")
    @PatchMapping(value = "/{userId}/password")
    public Result<Void> updatePassword(@Parameter(description = "用户ID") @PathVariable Long userId,
                                       @Parameter(description = "密码") @RequestParam String password) {
        userService.updatePassword(userId, password);
        return Result.success();
    }

    @PreAuthorize("@pms.hasPerm('sys:user:update:status')")
    @Operation(summary = "修改用户状态")
    @PatchMapping(value = "/{userId}/status")
    public Result<Void> updateUserPassword(@Parameter(description = "用户ID") @PathVariable Long userId,
                                           @Parameter(description = "用户状态") @RequestParam Integer status) {
        userService.update(new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getId, userId)
                .set(SysUser::getStatus, status));
        return Result.success();
    }

    @Operation(summary = "获取登录用户信息")
    @GetMapping("/info")
    public Result<UserLoginVO> getLoginUserInfo() {
        UserLoginVO userLoginVO = userService.getLoginUserInfo();
        return Result.success(userLoginVO);
    }

    @Operation(summary = "登出")
    @PostMapping("/logout")
    public Result<Void> logout() {
        userService.logout();
        return Result.success();
    }

    @Operation(summary = "用户导入模板下载")
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        String fileName = "用户导入模板.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));

        String fileClassPath = "excel-templates" + File.separator + fileName;
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileClassPath);

        ServletOutputStream outputStream = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(outputStream).withTemplate(inputStream).build();

        excelWriter.finish();
    }

    @Operation(summary = "导入用户")
    @PostMapping("/_import")
    public Result<String> importUsers(@Parameter(description = "部门ID") Long deptId,
                                      @Parameter(description = "角色ID") String roleIds,
                                      MultipartFile file) throws IOException {
        UserImportListener listener = new UserImportListener(deptId, roleIds);
        EasyExcel.read(file.getInputStream(), UserImportVO.class, listener).sheet().doRead();
        String msg = listener.getMsg();
        return Result.success(msg);
    }

    @Operation(summary = "导出用户")
    @GetMapping("/_export")
    public void exportUsers(UserPageQuery queryParams, HttpServletResponse response) throws IOException {
        String fileName = "用户列表.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));

        List<UserExportVO> exportUserList = userService.listExportUsers(queryParams);
        EasyExcel.write(response.getOutputStream(), UserExportVO.class).sheet("用户列表")
                .doWrite(exportUserList);
    }
}