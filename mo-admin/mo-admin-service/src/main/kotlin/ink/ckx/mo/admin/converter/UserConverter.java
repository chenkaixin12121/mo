package ink.ckx.mo.admin.converter;

import ink.ckx.mo.admin.api.model.entity.SysUser;
import ink.ckx.mo.admin.api.model.form.UserForm;
import ink.ckx.mo.admin.api.model.vo.user.UserImportVO;
import ink.ckx.mo.admin.api.model.vo.user.UserLoginVO;
import org.mapstruct.*;

/**
 * 用户对象转换器
 *
 * @author chenkaixin
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserConverter {

    @InheritInverseConfiguration(name = "entity2Form")
    SysUser form2Entity(UserForm entity);

    @Mappings({
            @Mapping(target = "userId", source = "id")
    })
    UserLoginVO entity2LoginUser(SysUser entity);

    @Mapping(target = "gender", expression = "java((ink.ckx.mo.common.web.enums.GenderEnum) ink.ckx.mo.common.core.base.IBaseEnum.getEnumByLabel(vo.getGender(), ink.ckx.mo.common.web.enums.GenderEnum.class))")
    SysUser importVO2Entity(UserImportVO vo);
}