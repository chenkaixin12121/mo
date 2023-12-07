package ink.ckx.mo.member.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.ckx.mo.member.api.model.dto.MemberUserInfoDTO;
import ink.ckx.mo.member.api.model.entity.MemberUser;
import ink.ckx.mo.member.api.model.vo.UserPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * @author chenkaixin
 * @description
 * @since 2023/12/20
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserConverter {

    @Mapping(target = "userId", source = "id")
    MemberUserInfoDTO entity2InfoDTO(MemberUser entity);

    Page<UserPageVO> entity2PageVO(Page<MemberUser> entity);
}