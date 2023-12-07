package ink.ckx.mo.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@EnableDiscoveryClient
@SpringBootApplication
class MoGatewayApplication

fun main(args: Array<String>) {
    runApplication<MoGatewayApplication>(*args)
}