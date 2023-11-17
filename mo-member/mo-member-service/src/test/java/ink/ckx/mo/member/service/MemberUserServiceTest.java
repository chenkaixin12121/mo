package ink.ckx.mo.member.service;

import ink.ckx.mo.common.web.enums.StatusEnum;
import ink.ckx.mo.member.api.model.entity.MemberUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberUserServiceTest {

    @Autowired
    private MemberUserService userService;

    @Test
    void insertUser() {
        MemberUser memberUser = new MemberUser();
        memberUser.setMobile("18312341234");
        memberUser.setNickName("陈开心");
        memberUser.setStatus(StatusEnum.ENABLE);
        userService.save(memberUser);
    }
}