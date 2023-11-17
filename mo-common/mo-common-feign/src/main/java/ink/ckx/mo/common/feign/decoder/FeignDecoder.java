package ink.ckx.mo.common.feign.decoder;

import cn.hutool.http.HttpStatus;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import ink.ckx.mo.common.core.result.Result;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
public class FeignDecoder implements Decoder {

    private final SpringDecoder decoder;

    public FeignDecoder(SpringDecoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        Method method = response.request().requestTemplate().methodMetadata().method();
        boolean notTheSame = method.getReturnType() != Result.class;
        if (notTheSame) {
            Type newType = new ParameterizedType() {
                @Override
                public Type[] getActualTypeArguments() {
                    return new Type[]{type};
                }

                @Override
                public Type getRawType() {
                    return Result.class;
                }

                @Override
                public Type getOwnerType() {
                    return null;
                }
            };
            Result<?> result = (Result<?>) this.decoder.decode(response, newType);
            if (Result.isSuccess(result)) {
                return result.getData();
            } else {
                throw new DecodeException(HttpStatus.HTTP_INTERNAL_ERROR, result.getMsg(), response.request());
            }
        }
        return this.decoder.decode(response, type);
    }
}