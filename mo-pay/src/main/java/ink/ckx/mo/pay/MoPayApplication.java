package ink.ckx.mo.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/16
 */
@EnableDiscoveryClient
@SpringBootApplication
public class MoPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoPayApplication.class, args);
    }
}