package ink.ckx.mo.admin.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.ckx.mo.admin.api.model.entity.SysDictItem;
import ink.ckx.mo.admin.api.model.form.DictItemForm;
import ink.ckx.mo.admin.api.model.vo.dict.DictItemDetailVO;
import ink.ckx.mo.admin.api.model.vo.dict.DictItemPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 字典数据项对象转换器
 *
 * @author chenkaixin
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DictItemConverter {

    Page<DictItemPageVO> entity2Page(Page<SysDictItem> page);

    DictItemDetailVO entity2DetailVO(SysDictItem entity);

    SysDictItem form2Entity(DictItemForm entity);
}