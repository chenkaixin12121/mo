package ink.ckx.mo.admin.service

import cn.hutool.core.collection.CollUtil
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import ink.ckx.mo.admin.api.model.entity.SysMenu
import ink.ckx.mo.admin.api.model.entity.SysRoleMenu
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest
internal class SysUserServiceTest @Autowired constructor(
    private val passwordEncoder: PasswordEncoder,
    private val menuService: SysMenuService,
    private val roleMenuService: SysRoleMenuService,
) {

    @Test
    fun printPassword() {
        println(passwordEncoder.encode("123456"))
    }

    @Test
    fun insertRoleMenu() {
        val menuIdList = menuService.listObjs(
            LambdaQueryWrapper<SysMenu>()
                .select(SysMenu::id)
        ) { e -> e.toString().toLong() }
        if (CollUtil.isNotEmpty(menuIdList)) {
            val roleMenuList = menuIdList.map { e -> SysRoleMenu(null, 2, e) }.toList()
            roleMenuService.saveBatch(roleMenuList)
        }
    }
}