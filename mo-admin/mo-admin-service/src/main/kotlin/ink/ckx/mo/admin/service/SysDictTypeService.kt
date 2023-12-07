package ink.ckx.mo.admin.service

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import ink.ckx.mo.admin.api.model.entity.SysDictType
import ink.ckx.mo.admin.api.model.form.DictTypeForm
import ink.ckx.mo.admin.api.model.query.DictTypePageQuery
import ink.ckx.mo.admin.api.model.vo.dict.DictTypeDetailVO
import ink.ckx.mo.admin.api.model.vo.dict.DictTypePageVO
import ink.ckx.mo.common.web.model.Option

/**
 * 数据字典类型业务接口
 *
 * @author chenkaixin
 */
interface SysDictTypeService : IService<SysDictType> {

    /**
     * 字典类型分页列表
     *
     * @param dictTypePageQuery 分页查询对象
     * @return
     */
    fun listDictTypePages(dictTypePageQuery: DictTypePageQuery): Page<DictTypePageVO>

    /**
     * 获取字典类型表单详情
     *
     * @param typeId 字典类型ID
     * @return
     */
    fun getDictTypeFormData(typeId: Long): DictTypeDetailVO?

    /**
     * 新增字典类型
     *
     * @param dictTypeForm 字典类型表单
     * @return
     */
    fun saveDictType(dictTypeForm: DictTypeForm): Long?

    /**
     * 修改字典类型
     *
     * @param typeId
     * @param dictTypeForm 字典类型表单
     * @return
     */
    fun updateDictType(typeId: Long, dictTypeForm: DictTypeForm)

    /**
     * 删除字典类型
     *
     * @param ids 字典类型ID，多个以英文逗号(,)分割
     * @return
     */
    fun deleteDictTypes(ids: String)

    /**
     * 获取字典类型的数据项
     *
     * @param typeCode
     * @return
     */
    fun listDictItemsByTypeCode(typeCode: String): List<Option<String>>
}