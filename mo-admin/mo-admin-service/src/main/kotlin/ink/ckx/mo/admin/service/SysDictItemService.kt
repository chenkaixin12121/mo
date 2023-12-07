package ink.ckx.mo.admin.service

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import ink.ckx.mo.admin.api.model.entity.SysDictItem
import ink.ckx.mo.admin.api.model.form.DictItemForm
import ink.ckx.mo.admin.api.model.query.DictItemPageQuery
import ink.ckx.mo.admin.api.model.vo.dict.DictItemDetailVO
import ink.ckx.mo.admin.api.model.vo.dict.DictItemPageVO

/**
 *
 */
interface SysDictItemService : IService<SysDictItem> {

    /**
     * 字典数据项分页列表
     *
     * @param dictItemPageQuery
     * @return
     */
    fun listDictItemPages(dictItemPageQuery: DictItemPageQuery): Page<DictItemPageVO>

    /**
     * 字典数据项表单详情
     *
     * @param itemId 字典数据项ID
     * @return
     */
    fun getDictItemDetail(itemId: Long): DictItemDetailVO?

    /**
     * 新增字典数据项
     *
     * @param dictItemForm 字典数据项表单
     * @return
     */
    fun saveDictItem(dictItemForm: DictItemForm): Long?

    /**
     * 修改字典数据项
     *
     * @param itemId       字典数据项ID
     * @param dictItemForm 字典数据项表单
     * @return
     */
    fun updateDictItem(itemId: Long, dictItemForm: DictItemForm)

    /**
     * 删除字典数据项
     *
     * @param ids 字典数据项ID，多个以英文逗号(,)分割
     * @return
     */
    fun deleteDictItems(ids: String)
}