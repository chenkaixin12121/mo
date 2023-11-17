package ink.ckx.mo.common.core.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/06
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResultCode {

    SUCCESS("0", "成功"),
    FAIL("1", "失败"),

    AUTH_ERROR("A001", "认证异常"),
    AUTH_INSUFFICIENT_EXCEPTION("A002", "需要完全身份验证才能访问此资源"),
    INVALID_TOKEN("A003", "token无效或已过期"),
    ACCESS_DENIED("A004", "无权限访问"),
    MOBILE_NOT_EMPTY("A005", "手机号不能为空"),
    SMS_CODE_NOT_EMPTY("A006", "短信验证码不能为空"),
    SMS_CODE_VERIFY_FAIL("A007", "短信验证码不正确"),
    USERNAME_NOT_EMPTY("A008", "用户名不能为空"),
    PASSWORD_NOT_EMTPY("A009", "密码不能为空"),
    PASSWORD_VERIFY_FAIL("A010", "密码不正确"),
    USER_NOT_EXIST("A011", "用户不存在"),
    USER_DISABLE("A011", "该用户已禁用"),
    INVALID_CLIENT("A012", "客户端认证失败"),
    UNSUPPORTED_GRANT_TYPE("A013", "无效的授权类型"),
    INVALID_REQUEST("A014", "无效的请求"),
    INVALID_GRANT("A015", "无效的授权"),
    INVALID_SCOPE("A016", "无效的授权范围"),
    SERVER_ERROR("A017", "授权服务器异常"),
    CODE_FAIL("A018", "验证码不正确"),
    RESOURCE_VISIT_ERROR("A019", "前后台资源不能相互访问"),
    VERIFY_CODE_NOT_EMPTY("A020", "验证码不能为空"),
    VERIFY_CODE_KEY_NOT_EMPTY("A021", "验证码key不能为空"),

    PARAM_ERROR("B001", "参数异常"),
    RESOURCE_NOT_FOUND("B002", "请求资源不存在"),
    FILE_UPLOAD_ERROR("B003", "文件上传失败"),
    SEND_SMS_CODE_ERROR("B004", "短信验证码发送失败"),
    PAY_ERROR("B005", "支付失败"),
    ;

    private String code;

    private String msg;
}