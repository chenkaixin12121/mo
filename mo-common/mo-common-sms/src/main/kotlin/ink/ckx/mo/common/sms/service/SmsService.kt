package ink.ckx.mo.common.sms.service

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
interface SmsService {

    fun sendSmsCode(phoneNumber: String)
}