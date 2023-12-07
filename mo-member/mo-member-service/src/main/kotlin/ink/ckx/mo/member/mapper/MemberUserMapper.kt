package ink.ckx.mo.member.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import ink.ckx.mo.member.api.model.entity.MemberUser
import org.apache.ibatis.annotations.Mapper

@Mapper
interface MemberUserMapper : BaseMapper<MemberUser>