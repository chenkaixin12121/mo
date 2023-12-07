package ink.ckx.mo.common.core.constant

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/09
 */
interface CoreConstant {

    companion object {

        /**
         * 授权信息中的权限或角色的 key
         */
        const val AUTHORITIES_CLAIM_NAME_KEY = "authorities"

        /**
         * 密码模式（自定义）
         */
        const val GRANT_TYPE_CUSTOM_PASSWORD = "password"

        /**
         * 验证码模式（自定义）
         */
        const val GRANT_TYPE_CUSTOM_CAPTCHA = "authorization_captcha"

        /**
         * 短信验证码模式（自定义）
         */
        const val GRANT_TYPE_CUSTOM_MOBILE = "authorization_mobile"

        /**
         * 手机号
         */
        const val MOBILE = "mobile"

        /**
         * 短信验证码
         */
        const val SMS_CODE = "sms_code"

        /**
         * 短信验证码默认值
         */
        const val SMS_CODE_VALUE = "8888"

        /**
         * 短信验证码key
         */
        const val SMS_CODE_PREFIX = "AUTH:SMS_CODE:"

        /**
         * 短信验证码缓存时间
         */
        const val SMS_CODE_CACHE_EXPIRE = 600

        /**
         * 验证码缓存key
         */
        const val VERIFY_CODE_CACHE_KEY_PREFIX = "AUTH:VERIFY_CODE:"

        /**
         * 验证码缓存时间
         */
        const val VERIFY_CODE_CACHE_EXPIRE = 300

        /**
         * 验证码
         */
        const val VERIFY_CODE = "verifyCode"

        /**
         * 验证码缓存Key
         */
        const val VERIFY_CODE_KEY = "verifyCodeKey"

        /**
         * knife4j 客户端
         */
        const val MO_KNIFE4J_CLIENT_ID = "mo-knife4j-client"

        /**
         * 后台 客户端
         */
        const val MO_ADMIN_CLIENT_ID = "mo-admin-client"

        /**
         * 前台 App 客户端
         */
        const val MO_PORTAL_CLIENT_ID = "mo-portal-app-client"

        /**
         * App接口路径匹配
         */
        const val APP_API_PATTERN = "/appApi/**"

        /**
         * 角色权限集合缓存前缀
         */
        const val ROLE_PERMS_CACHE_KEY_PREFIX = "AUTH:ROLE_PERMS:"

        /**
         * 超级管理员角色编码
         */
        const val SUPER_ADMIN_CODE = "SUPER-ADMIN"

        /**
         * 系统默认密码
         */
        const val DEFAULT_USER_PASSWORD = "123456"

        /**
         * token 黑名单
         */
        const val TOKEN_BLACK = "TOKEN_BLACK:"

        /**
         * 用户id
         */
        const val USER_ID = "userId"

        /**
         * 部门id
         */
        const val DEPT_ID = "deptId"

        /**
         * 数据权限
         */
        const val DATA_SCOPE = "dataScope"

        /**
         * 内部
         */
        const val FROM_IN = "Y"

        /**
         * 标志
         */
        const val FROM = "from"

        /**
         * 排除静态资源
         */
        val IGNORE_STATIC_LIST =
            listOf("/webjars/**", "/doc.html", "/swagger-resources/**", "/v3/api-docs/**", "/swagger-ui/**")
    }
}