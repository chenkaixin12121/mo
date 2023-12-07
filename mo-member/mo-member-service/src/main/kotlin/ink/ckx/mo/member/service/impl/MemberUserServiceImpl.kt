package ink.ckx.mo.member.service.impl

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import ink.ckx.mo.member.api.model.dto.MemberUserInfoDTO
import ink.ckx.mo.member.api.model.entity.MemberUser
import ink.ckx.mo.member.api.model.query.UserPageQuery
import ink.ckx.mo.member.api.model.vo.UserPageVO
import ink.ckx.mo.member.converter.UserConverter
import ink.ckx.mo.member.mapper.MemberUserMapper
import ink.ckx.mo.member.service.MemberUserService
import org.springframework.stereotype.Service

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/03
 */
@Service
class MemberUserServiceImpl(
    private var userConverter: UserConverter
) : ServiceImpl<MemberUserMapper, MemberUser>(), MemberUserService {

    override fun getUserAuthInfo(mobile: String): MemberUserInfoDTO? {
        val memberUser = ktQuery()
            .select(MemberUser::id, MemberUser::mobile, MemberUser::status)
            .eq(MemberUser::mobile, mobile)
            .one()
        return userConverter.entity2InfoDTO(memberUser)
    }

    override fun listUserPages(userPageQuery: UserPageQuery): IPage<UserPageVO> {
        val memberUserPage = ktQuery()
            .select(
                MemberUser::id,
                MemberUser::mobile,
                MemberUser::status,
                MemberUser::nickName,
                MemberUser::gender,
                MemberUser::avatarUrl,
                MemberUser::birthday,
                MemberUser::createTime
            )
            .eq(!userPageQuery.keywords.isNullOrBlank(), MemberUser::mobile, userPageQuery.keywords)
            .page(Page(userPageQuery.pageNum, userPageQuery.pageSize))
        return userConverter.entity2PageVO(memberUserPage)
    }
}