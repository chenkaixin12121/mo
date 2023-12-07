package ink.ckx.mo.admin.mapper

import com.baomidou.mybatisplus.core.conditions.Wrapper
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.core.toolkit.Constants
import ink.ckx.mo.admin.api.model.entity.SysDept
import ink.ckx.mo.common.mybatis.annotation.DataPermission
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface SysDeptMapper : BaseMapper<SysDept> {

    @DataPermission(deptIdColumnName = "id")
    override fun selectList(@Param(Constants.WRAPPER) queryWrapper: Wrapper<SysDept>): List<SysDept>
}