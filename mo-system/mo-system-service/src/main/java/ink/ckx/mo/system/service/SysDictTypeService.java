package ink.ckx.mo.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import ink.ckx.mo.common.web.model.Option;
import ink.ckx.mo.system.api.model.entity.SysDictType;
import ink.ckx.mo.system.api.model.form.DictTypeForm;
import ink.ckx.mo.system.api.model.query.DictTypePageQuery;
import ink.ckx.mo.system.api.model.vo.dict.DictTypeDetailVO;
import ink.ckx.mo.system.api.model.vo.dict.DictTypePageVO;

import java.util.List;

/**
 * 数据字典类型业务接口
 *
 * @author chenkaixin
 */
public interface SysDictTypeService extends IService<SysDictType> {

    /**
     * 字典类型分页列表
     *
     * @param dictTypePageQuery 分页查询对象
     * @return
     */
    Page<DictTypePageVO> listDictTypePages(DictTypePageQuery dictTypePageQuery);

    /**
     * 获取字典类型表单详情
     *
     * @param typeId 字典类型ID
     * @return
     */
    DictTypeDetailVO getDictTypeFormData(Long typeId);

    /**
     * 新增字典类型
     *
     * @param dictTypeForm 字典类型表单
     * @return
     */
    Long saveDictType(DictTypeForm dictTypeForm);

    /**
     * 修改字典类型
     *
     * @param typeId
     * @param dictTypeForm 字典类型表单
     * @return
     */
    void updateDictType(Long typeId, DictTypeForm dictTypeForm);

    /**
     * 删除字典类型
     *
     * @param ids 字典类型ID，多个以英文逗号(,)分割
     * @return
     */
    void deleteDictTypes(String ids);

    /**
     * 获取字典类型的数据项
     *
     * @param typeCode
     * @return
     */
    List<Option<String>> listDictItemsByTypeCode(String typeCode);
}