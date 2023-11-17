package ink.ckx.mo.system.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.ckx.mo.system.api.model.entity.SysDictType;
import ink.ckx.mo.system.api.model.form.DictTypeForm;
import ink.ckx.mo.system.api.model.vo.dict.DictTypeDetailVO;
import ink.ckx.mo.system.api.model.vo.dict.DictTypePageVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 字典类型对象转换器
 *
 * @author chenkaixin
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DictTypeConverter {

    Page<DictTypePageVO> entity2Page(Page<SysDictType> page);

    DictTypeDetailVO entity2DetailVO(SysDictType entity);

    SysDictType form2Entity(DictTypeForm entity);
}
