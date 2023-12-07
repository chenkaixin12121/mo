package ink.ckx.mo.member.service

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.service.IService
import ink.ckx.mo.member.api.model.dto.MemberUserInfoDTO
import ink.ckx.mo.member.api.model.entity.MemberUser
import ink.ckx.mo.member.api.model.query.UserPageQuery
import ink.ckx.mo.member.api.model.vo.UserPageVO

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/03
 */
interface MemberUserService : IService<MemberUser> {

    /**
     * 根据手机号查询用户
     *
     * @param mobile
     * @return
     */
    fun getUserAuthInfo(mobile: String): MemberUserInfoDTO?

    /**
     * 用户分页
     *
     * @param userPageQuery
     * @return
     */
    fun listUserPages(userPageQuery: UserPageQuery): IPage<UserPageVO>
}