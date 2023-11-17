package ink.ckx.mo.member.controller.app;

import ink.ckx.mo.common.core.result.Result;
import ink.ckx.mo.common.security.inner.Inner;
import ink.ckx.mo.member.api.model.dto.MemberUserInfoDTO;
import ink.ckx.mo.member.service.MemberUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@Tag(name = "App用户接口")
@RestController
@RequestMapping("/appApi/v1/users")
@RequiredArgsConstructor
public class AppMemberUserController {

    private final MemberUserService userService;

    @Inner
    @Operation(summary = "用户信息")
    @GetMapping("/{mobile}/authInfo")
    public Result<MemberUserInfoDTO> getUserAuthInfo(@Parameter(description = "手机号") @PathVariable String mobile) {
        MemberUserInfoDTO memberUserInfoDTO = userService.getUserAuthInfo(mobile);
        return Result.success(memberUserInfoDTO);
    }

    @Operation(summary = "测试接口")
    @GetMapping("/test")
    public Result<Void> test() {
        return Result.success();
    }
}