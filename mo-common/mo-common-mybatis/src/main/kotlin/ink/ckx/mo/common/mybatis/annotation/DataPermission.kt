package ink.ckx.mo.common.mybatis.annotation

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/21
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class DataPermission(

    /**
     * 数据权限 [com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor]
     */
    val deptAlias: String = "",

    val deptIdColumnName: String = "dept_id",

    val userAlias: String = "",

    val userIdColumnName: String = "id"
)