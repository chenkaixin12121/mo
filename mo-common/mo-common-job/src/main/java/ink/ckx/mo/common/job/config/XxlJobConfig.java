package ink.ckx.mo.common.job.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/16
 */
@Configuration
@Slf4j
public class XxlJobConfig {

    @Value("${xxl.job.address}")
    private String xxlJobAddress;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private Integer serverPort;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobAddress);
        xxlJobSpringExecutor.setAppname(applicationName);
        xxlJobSpringExecutor.setAddress(null);
        xxlJobSpringExecutor.setIp(null);
        xxlJobSpringExecutor.setPort(serverPort / 10);
        xxlJobSpringExecutor.setAccessToken(null);
        xxlJobSpringExecutor.setLogPath(null);
        xxlJobSpringExecutor.setLogRetentionDays(30);
        return xxlJobSpringExecutor;
    }

    /**
     * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
     *
     *      1、引入依赖：
     *          <dependency>
     *             <groupId>org.springframework.cloud</groupId>
     *             <artifactId>spring-cloud-commons</artifactId>
     *             <version>${version}</version>
     *         </dependency>
     *
     *      2、配置文件，或者容器启动变量
     *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
     *
     *      3、获取IP
     *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
     */
}