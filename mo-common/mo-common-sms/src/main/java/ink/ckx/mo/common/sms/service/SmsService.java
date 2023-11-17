package ink.ckx.mo.common.sms.service;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
public interface SmsService {

    void sendSmsCode(String phoneNumber);
}
