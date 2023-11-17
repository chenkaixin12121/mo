package ink.ckx.mo.system.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import ink.ckx.mo.system.api.model.entity.SysMenu;
import ink.ckx.mo.system.api.model.entity.SysRoleMenu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
class SysUserServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysMenuService menuService;

    @Autowired
    private SysRoleMenuService roleMenuService;

    @Test
    void printPassword() {
        System.out.println(passwordEncoder.encode("123456"));
    }

    @Test
    void insertRoleMenu() {
        List<Long> menuIdList = menuService.listObjs(new LambdaQueryWrapper<SysMenu>()
                .select(SysMenu::getId), e -> Long.valueOf(e.toString()));
        if (CollUtil.isNotEmpty(menuIdList)) {
            List<SysRoleMenu> roleMenuList = menuIdList.stream().map(e -> new SysRoleMenu(null, 2L, e)).toList();
            roleMenuService.saveBatch(roleMenuList);
        }
    }
}