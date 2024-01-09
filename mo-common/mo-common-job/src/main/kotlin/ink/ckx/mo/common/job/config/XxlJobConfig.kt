package ink.ckx.mo.common.job.config

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/16
 */
@Configuration
class XxlJobConfig {

    @Value("\${xxl.job.adminAddress}")
    private val xxlJobAdminAddress: String? = null

    @Value("\${spring.application.name}")
    private val applicationName: String? = null

    @Value("\${server.port}")
    private val serverPort: Int? = null

    private val log = KotlinLogging.logger {}

    @Bean
    fun xxlJobExecutor(): XxlJobSpringExecutor {
        log.info { ">>>>>>>>>>> xxl-job config init." }
        val xxlJobSpringExecutor = XxlJobSpringExecutor()
        xxlJobSpringExecutor.setAdminAddresses(xxlJobAdminAddress)
        xxlJobSpringExecutor.setAppname(applicationName)
        xxlJobSpringExecutor.setAddress(null)
        xxlJobSpringExecutor.setIp(null)
        xxlJobSpringExecutor.setPort(serverPort!! / 10)
        xxlJobSpringExecutor.setAccessToken("chenkaixin12121")
        xxlJobSpringExecutor.setLogPath(null)
        xxlJobSpringExecutor.setLogRetentionDays(30)
        return xxlJobSpringExecutor
    }

    /**
     * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
     *
     * 1、引入依赖：
     * <dependency>
     * <groupId>org.springframework.cloud</groupId>
     * <artifactId>spring-cloud-commons</artifactId>
     * <version>${version}</version>
    </dependency> *
     *
     * 2、配置文件，或者容器启动变量
     * spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
     *
     * 3、获取IP
     * String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
     */
}