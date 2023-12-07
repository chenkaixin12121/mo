package ink.ckx.mo.common.mybatis.result

import com.baomidou.mybatisplus.core.metadata.IPage
import ink.ckx.mo.common.core.result.ResultCode
import java.io.Serializable

/**
 * @author chenkaixin
 * @description
 * @since 2023/10/19
 */
data class PageResult<T>(
    var code: String,
    var data: Data<T>,
    var msg: String,
) : Serializable {
    companion object {
        @JvmStatic
        fun <T> success(page: IPage<T>): PageResult<T> {
            return PageResult(ResultCode.SUCCESS.code, Data(page.records, page.total.toInt()), ResultCode.SUCCESS.msg)
        }
    }
}

data class Data<T>(
    var list: List<T>,
    var total: Int
) : Serializable