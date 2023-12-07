package ink.ckx.mo.admin.converter;

import ink.ckx.mo.admin.api.model.entity.SysMenu;
import ink.ckx.mo.admin.api.model.form.MenuForm;
import ink.ckx.mo.admin.api.model.vo.menu.MenuDetailVO;
import ink.ckx.mo.admin.api.model.vo.menu.MenuListVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 菜单对象转换器
 *
 * @author chenkaixin
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MenuConverter {

    MenuListVO entity2VO(SysMenu entity);

    MenuDetailVO entity2DetailVO(SysMenu entity);

    SysMenu from2Entity(MenuForm from);
}