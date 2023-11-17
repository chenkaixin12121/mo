package ink.ckx.mo.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import ink.ckx.mo.common.core.result.Result;
import ink.ckx.mo.common.core.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
public class OAuth2EndpointUtils {

    public OAuth2EndpointUtils() {
    }

    public static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            if (values.length > 0) {
                for (String value : values) {
                    parameters.add(key, value);
                }
            }
        });
        return parameters;
    }

    public static void fail(HttpServletResponse response, ResultCode resultCode) {
        fail(response, resultCode, null);
    }

    @SneakyThrows
    public static void fail(HttpServletResponse response, ResultCode resultCode, String msg) {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), Result.fail(resultCode, msg));
    }
}