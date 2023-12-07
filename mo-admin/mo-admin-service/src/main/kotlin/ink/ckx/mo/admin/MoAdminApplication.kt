package ink.ckx.mo.admin

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
class MoAdminApplication

fun main(args: Array<String>) {
    runApplication<MoAdminApplication>(*args)
}