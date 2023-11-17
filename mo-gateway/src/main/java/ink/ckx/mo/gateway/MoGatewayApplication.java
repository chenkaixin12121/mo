package ink.ckx.mo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@EnableDiscoveryClient
@SpringBootApplication
public class MoGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoGatewayApplication.class, args);
    }
}
