package ink.ckx.mo.common.security.inner;

import java.lang.annotation.*;

/**
 * @author chenkaixin
 * @description 开放服务间调用
 * @since 2023/11/12
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inner {

    /**
     * 是否 AOP统 一处理
     *
     * @return false, true
     */
    boolean value() default true;
}
