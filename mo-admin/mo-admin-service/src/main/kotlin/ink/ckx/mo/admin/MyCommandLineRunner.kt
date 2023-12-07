package ink.ckx.mo.admin

import ink.ckx.mo.admin.service.SysMenuService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/13
 */
@Component
class MyCommandLineRunner(
    private val menuService: SysMenuService
) : CommandLineRunner {

    override fun run(vararg args: String) {
        menuService.refreshRolePerm()
    }
}