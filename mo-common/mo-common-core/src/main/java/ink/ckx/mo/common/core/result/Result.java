package ink.ckx.mo.common.core.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author chenkaixin
 * @description
 * @since 2023/10/19
 */
@NoArgsConstructor
@Data
public class Result<T> implements Serializable {

    private String code;

    private String msg;

    private T data;

    public Result(String code, String msg) {
        this(code, msg, null);
    }

    public Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    public static <T> Result<T> fail() {
        return Result.fail(ResultCode.FAIL);
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<>(ResultCode.FAIL.getCode(), msg);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMsg());
    }

    public static <T> Result<T> fail(ResultCode resultCode, String msg) {
        return new Result<>(resultCode.getCode(), Optional.ofNullable(msg).orElse(resultCode.getMsg()));
    }

    public static boolean isSuccess(Result<?> result) {
        return result != null && ResultCode.SUCCESS.getCode().equals(result.getCode());
    }
}