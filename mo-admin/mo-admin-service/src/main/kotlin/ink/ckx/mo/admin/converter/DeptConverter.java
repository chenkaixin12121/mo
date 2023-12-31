package ink.ckx.mo.admin.converter;

import ink.ckx.mo.admin.api.model.entity.SysDept;
import ink.ckx.mo.admin.api.model.form.DeptForm;
import ink.ckx.mo.admin.api.model.vo.dept.DeptDetailVO;
import ink.ckx.mo.admin.api.model.vo.dept.DeptListVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 部门对象转换器
 *
 * @author chenkaixin
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeptConverter {

    DeptDetailVO entity2DetailVO(SysDept entity);

    DeptListVO entity2VO(SysDept entity);

    SysDept form2Entity(DeptForm deptForm);
}