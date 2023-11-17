package ink.ckx.mo.member.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.ckx.mo.member.api.model.dto.MemberUserInfoDTO;
import ink.ckx.mo.member.api.model.entity.MemberUser;
import ink.ckx.mo.member.api.model.query.UserPageQuery;
import ink.ckx.mo.member.api.model.vo.UserPageVO;
import ink.ckx.mo.member.converter.UserConverter;
import ink.ckx.mo.member.mapper.MemberUserMapper;
import ink.ckx.mo.member.service.MemberUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/03
 */
@RequiredArgsConstructor
@Service
public class MemberUserServiceImpl extends ServiceImpl<MemberUserMapper, MemberUser> implements MemberUserService {

    private final UserConverter userConverter;

    @Override
    public MemberUserInfoDTO getUserAuthInfo(String mobile) {
        MemberUser memberUser = this.getOne(new LambdaQueryWrapper<MemberUser>()
                .select(MemberUser::getId, MemberUser::getMobile, MemberUser::getStatus)
                .eq(MemberUser::getMobile, mobile));

        return userConverter.entity2InfoDTO(memberUser);
    }

    @Override
    public IPage<UserPageVO> listUserPages(UserPageQuery userPageQuery) {
        Page<MemberUser> memberUserPage = this.page(new Page<>(userPageQuery.getPageNum(), userPageQuery.getPageSize()), new LambdaQueryWrapper<MemberUser>()
                .select(MemberUser::getId, MemberUser::getMobile, MemberUser::getStatus, MemberUser::getNickName, MemberUser::getGender, MemberUser::getAvatarUrl, MemberUser::getBirthday, MemberUser::getCreateTime)
                .eq(StrUtil.isNotBlank(userPageQuery.getKeywords()), MemberUser::getMobile, userPageQuery.getKeywords()));
        return userConverter.entity2PageVO(memberUserPage);
    }
}