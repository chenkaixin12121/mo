package ink.ckx.mo.common.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/21
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DataPermission {

    /**
     * 数据权限 {@link com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor}
     */
    String deptAlias() default "";

    String deptIdColumnName() default "dept_id";

    String userAlias() default "";

    String userIdColumnName() default "id";
}