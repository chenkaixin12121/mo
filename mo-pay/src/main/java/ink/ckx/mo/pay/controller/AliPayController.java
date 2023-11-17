package ink.ckx.mo.pay.controller;

import ink.ckx.mo.common.core.result.Result;
import ink.ckx.mo.pay.service.AliPayService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/16
 */
@Validated
@Slf4j
@Tag(name = "支付宝支付")
@RequiredArgsConstructor
@RequestMapping(value = "/appApi/v1/aliPay")
@RestController
public class AliPayController {

    private final AliPayService aliPayService;

    @Operation(summary = "获取支付宝支付表单")
    @PostMapping("/getAliPayForm")
    public Result<String> getAliPayForm(@Parameter(description = "订单号") @NotBlank(message = "订单号不能为空") String orderNo,
                                        @Parameter(description = "金额") @NotNull(message = "金额不能为空") BigDecimal amount) {
        String aliPayForm = aliPayService.getAliPayForm(orderNo, amount);
        return Result.success(aliPayForm);
    }

    @Hidden
    @Operation(summary = "支付成功后的异步通知")
    @PostMapping("/notice")
    public String notice(@Parameter(hidden = true) HttpServletRequest request) {
        return aliPayService.notice(request);
    }

    @Hidden
    @Operation(summary = "支付成功后跳转的界面")
    @GetMapping("/go")
    public void go(@Parameter(hidden = true) HttpServletRequest request) {
        log.info("订单：{}，支付成功后跳转界面", request.getParameter("out_trade_no"));
    }
}