package ink.ckx.mo.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.ckx.mo.member.api.model.entity.MemberUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/03
 */
@Mapper
public interface MemberUserMapper extends BaseMapper<MemberUser> {
}