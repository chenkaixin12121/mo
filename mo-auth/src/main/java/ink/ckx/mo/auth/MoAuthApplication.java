package ink.ckx.mo.auth;

import ink.ckx.mo.member.api.feign.RemoteMemberUserService;
import ink.ckx.mo.system.api.feign.RemoteSysUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/09
 */
@EnableFeignClients(basePackageClasses = {RemoteSysUserService.class, RemoteMemberUserService.class})
@EnableDiscoveryClient
@SpringBootApplication
public class MoAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoAuthApplication.class, args);
    }
}