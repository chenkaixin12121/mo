package ink.ckx.mo.auth

import ink.ckx.mo.admin.api.feign.RemoteSysUserService
import ink.ckx.mo.member.api.feign.RemoteMemberUserService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/09
 */
@EnableFeignClients(basePackageClasses = [RemoteSysUserService::class, RemoteMemberUserService::class])
@EnableDiscoveryClient
@SpringBootApplication
class MoAuthApplication

fun main(args: Array<String>) {
    runApplication<MoAuthApplication>(*args)
}