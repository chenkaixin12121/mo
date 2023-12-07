package ink.ckx.mo.common.security.inner

/**
 * @author chenkaixin
 * @description 开放服务间调用
 * @since 2023/11/12
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Inner(

    /**
     * 是否 AOP统 一处理
     *
     * @return false, true
     */
    val value: Boolean = true
)