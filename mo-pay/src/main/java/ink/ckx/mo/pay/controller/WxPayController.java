package ink.ckx.mo.pay.controller;

import ink.ckx.mo.common.core.result.Result;
import ink.ckx.mo.pay.service.WxPayService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/16
 */
@Validated
@Tag(name = "微信支付")
@RequiredArgsConstructor
@RequestMapping(value = "/appApi/v1/wxPay")
@RestController
public class WxPayController {

    private final WxPayService wxPayService;

    @Operation(summary = "获取微信扫码支付页面地址")
    @PostMapping("/getWxPayQRCode")
    public Result<String> getWxPayQRCode(@NotBlank(message = "订单号不能方空") String orderNo,
                                         @NotNull(message = "金额不能方空") BigDecimal amount) {
        String qrCodeUrl = wxPayService.getWxPayQRCode(orderNo, amount);
        return Result.success(qrCodeUrl);
    }

    @Hidden
    @Operation(summary = "支付成功后的异步通知")
    @RequestMapping(value = "/notice")
    public void notice(@Parameter(hidden = true) HttpServletRequest request,
                       @Parameter(hidden = true) HttpServletResponse response) throws IOException {
        wxPayService.notice(request, response);
    }
}