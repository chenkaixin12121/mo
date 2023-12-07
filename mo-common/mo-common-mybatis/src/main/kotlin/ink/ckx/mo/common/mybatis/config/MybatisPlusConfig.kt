package ink.ckx.mo.common.mybatis.config

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.core.config.GlobalConfig
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import ink.ckx.mo.common.mybatis.handler.MyDataPermissionHandler
import ink.ckx.mo.common.mybatis.handler.MyMetaObjectHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/09
 */
@Configuration
class MybatisPlusConfig {

    /**
     * 插件相关
     */
    @Bean
    fun mybatisPlusInterceptor(): MybatisPlusInterceptor {
        val interceptor = MybatisPlusInterceptor()
        // 分页
        interceptor.addInnerInterceptor(PaginationInnerInterceptor(DbType.MYSQL))
        // 防全表更新与删除插件
        interceptor.addInnerInterceptor(BlockAttackInnerInterceptor())
        // 数据权限
        interceptor.addInnerInterceptor(DataPermissionInterceptor(MyDataPermissionHandler()))
        return interceptor
    }

    /**
     * 自动填充创建时间、更新时间
     */
    @Bean
    fun globalConfig(): GlobalConfig {
        val globalConfig = GlobalConfig()
        globalConfig.metaObjectHandler = MyMetaObjectHandler()
        return globalConfig
    }
}