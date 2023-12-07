package ink.ckx.mo.common.sms.controller

import ink.ckx.mo.common.core.result.Result
import ink.ckx.mo.common.core.result.Result.Companion.success
import ink.ckx.mo.common.sms.query.SmsCodeQuery
import ink.ckx.mo.common.sms.service.SmsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
@Tag(name = "短信接口")
@RestController
@RequestMapping("/sms")
class SmsController(private val smsService: SmsService) {

    @Operation(summary = "发送验证码")
    @PostMapping("/verifyCode")
    fun sendSmsCode(@RequestBody @Valid smsCodeQuery: SmsCodeQuery): Result<Void?> {
        smsCodeQuery.phoneNumber?.let { smsService.sendSmsCode(it) }
        return success()
    }
}