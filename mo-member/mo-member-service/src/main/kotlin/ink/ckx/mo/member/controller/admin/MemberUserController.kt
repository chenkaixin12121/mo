package ink.ckx.mo.member.controller.admin

import ink.ckx.mo.common.core.result.Result
import ink.ckx.mo.common.core.result.Result.Companion.success
import ink.ckx.mo.common.mybatis.result.PageResult
import ink.ckx.mo.common.mybatis.result.PageResult.Companion.success
import ink.ckx.mo.member.api.model.query.UserPageQuery
import ink.ckx.mo.member.api.model.vo.UserPageVO
import ink.ckx.mo.member.service.MemberUserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@Tag(name = "用户接口")
@RestController
@RequestMapping("/api/v1/users")
class MemberUserController(private val userService: MemberUserService) {

    @Operation(summary = "用户分页列表")
    @PostMapping("/pages")
    fun listUserPages(@RequestBody userPageQuery: UserPageQuery): PageResult<UserPageVO> {
        val result = userService.listUserPages(userPageQuery)
        return success(result)
    }

    @Operation(summary = "测试接口")
    @GetMapping("/test")
    fun test(): Result<Void?> {
        return success()
    }
}