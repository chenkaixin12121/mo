package ink.ckx.mo.auth.util

import com.fasterxml.jackson.databind.ObjectMapper
import ink.ckx.mo.common.core.result.Result
import ink.ckx.mo.common.core.result.ResultCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
object OAuth2EndpointUtils {

    fun getParameters(request: HttpServletRequest): MultiValueMap<String, String> {
        val parameterMap: Map<String, Array<String>> = request.parameterMap
        val parameters = LinkedMultiValueMap<String, String>()
        parameterMap.forEach { (key: String, values: Array<String>) ->
            if (values.isNotEmpty()) {
                for (value in values) {
                    parameters.add(key, value)
                }
            }
        }
        return parameters
    }

    @JvmStatic
    fun fail(response: HttpServletResponse, resultCode: ResultCode) {
        fail(response, resultCode, null)
    }

    @JvmStatic
    fun fail(response: HttpServletResponse, resultCode: ResultCode, msg: String?) {
        response.contentType = "application/json"
        val mapper = ObjectMapper()
        mapper.writeValue(response.outputStream, Result.fail<Void>(resultCode, msg))
    }
}