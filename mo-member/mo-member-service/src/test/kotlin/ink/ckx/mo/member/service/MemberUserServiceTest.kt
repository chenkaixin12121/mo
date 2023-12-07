package ink.ckx.mo.member.service

import cn.hutool.json.JSONUtil
import ink.ckx.mo.common.web.enums.StatusEnum
import ink.ckx.mo.member.api.model.entity.MemberUser
import ink.ckx.mo.member.api.model.query.UserPageQuery
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MemberUserServiceTest @Autowired constructor(
    private val userService: MemberUserService
) {

    private val log = KotlinLogging.logger {}

    @Test
    fun insertUser() {
        val memberUser = MemberUser()
        memberUser.mobile = "18312341234"
        memberUser.nickName = "陈开心"
        memberUser.status = StatusEnum.ENABLE
        userService.save(memberUser)
    }

    @Test
    fun getUserAuthInfo() {
        val userAuthInfo = userService.getUserAuthInfo("123")
        log.info { "data: ${JSONUtil.toJsonPrettyStr(userAuthInfo)}" }
    }

    @Test
    fun listUserPages() {
        val pageQuery = UserPageQuery()
        val pages = userService.listUserPages(pageQuery)
        log.info { "data: ${JSONUtil.toJsonPrettyStr(pages)}" }
    }
}