package ink.ckx.mo.system;

import ink.ckx.mo.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/13
 */
@RequiredArgsConstructor
@Component
public class MyCommandLineRunner implements CommandLineRunner {

    private final SysMenuService menuService;

    @Override
    public void run(String... args) {
        menuService.refreshRolePerm();
    }
}