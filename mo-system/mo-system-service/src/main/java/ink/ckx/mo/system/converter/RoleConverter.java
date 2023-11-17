package ink.ckx.mo.system.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.ckx.mo.common.web.model.Option;
import ink.ckx.mo.system.api.model.entity.SysRole;
import ink.ckx.mo.system.api.model.form.RoleForm;
import ink.ckx.mo.system.api.model.vo.role.RoleDetailVO;
import ink.ckx.mo.system.api.model.vo.role.RolePageVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 角色对象转换器
 *
 * @author chenkaixin
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleConverter {

    Page<RolePageVO> entity2Page(Page<SysRole> page);

    @Mappings({
            @Mapping(target = "value", source = "id"),
            @Mapping(target = "label", source = "name")
    })
    Option<Long> role2Option(SysRole role);

    List<Option<Long>> roles2Options(List<SysRole> roles);

    SysRole form2Entity(RoleForm roleForm);

    RoleDetailVO entity2DetailVO(SysRole entity);
}