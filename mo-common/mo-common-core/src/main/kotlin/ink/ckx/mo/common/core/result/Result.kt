package ink.ckx.mo.common.core.result

import java.io.Serializable
import java.util.*

/**
 * @author chenkaixin
 * @description
 * @since 2023/10/19
 */
class Result<T>(
    val code: String? = null,
    val msg: String? = null,
    data: T? = null,
) : Serializable {

    val data: T?

    init {
        this.data = data
    }

    companion object {
        @JvmStatic
        fun <T> success(): Result<T?> {
            return success(null)
        }

        @JvmStatic
        fun <T> success(data: T): Result<T> {
            return Result(ResultCode.SUCCESS.code, ResultCode.SUCCESS.msg, data)
        }

        @JvmStatic
        fun <T> fail(): Result<T> {
            return fail(ResultCode.FAIL)
        }

        @JvmStatic
        fun <T> fail(msg: String): Result<T> {
            return Result(ResultCode.FAIL.code, msg)
        }

        @JvmStatic
        fun <T> fail(resultCode: ResultCode): Result<T> {
            return Result(resultCode.code, resultCode.msg)
        }

        @JvmStatic
        fun <T> fail(resultCode: ResultCode, msg: String?): Result<T> {
            return Result(resultCode.code, Optional.ofNullable(msg).orElse(resultCode.msg))
        }

        @JvmStatic
        fun isSuccess(result: Result<*>): Boolean {
            return ResultCode.SUCCESS.code == result.code
        }
    }
}