package ink.ckx.mo.member

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
class MoMemberApplication

fun main(args: Array<String>) {
    runApplication<MoMemberApplication>(*args)
}