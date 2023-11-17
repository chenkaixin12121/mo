package ink.ckx.mo.member.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import ink.ckx.mo.common.mybatis.result.PageResult;
import ink.ckx.mo.member.api.model.query.UserPageQuery;
import ink.ckx.mo.member.api.model.vo.UserPageVO;
import ink.ckx.mo.member.service.MemberUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@Tag(name = "用户接口")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class MemberUserController {

    private final MemberUserService userService;

    @Operation(summary = "用户分页列表")
    @PostMapping("/pages")
    public PageResult<UserPageVO> listUserPages(@RequestBody UserPageQuery userPageQuery) {
        IPage<UserPageVO> result = userService.listUserPages(userPageQuery);
        return PageResult.success(result);
    }
}