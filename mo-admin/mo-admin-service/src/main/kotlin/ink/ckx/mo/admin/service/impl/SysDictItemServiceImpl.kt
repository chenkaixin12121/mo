package ink.ckx.mo.admin.service.impl

import cn.hutool.core.util.StrUtil
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import ink.ckx.mo.admin.api.model.entity.SysDictItem
import ink.ckx.mo.admin.api.model.form.DictItemForm
import ink.ckx.mo.admin.api.model.query.DictItemPageQuery
import ink.ckx.mo.admin.api.model.vo.dict.DictItemDetailVO
import ink.ckx.mo.admin.api.model.vo.dict.DictItemPageVO
import ink.ckx.mo.admin.converter.DictItemConverter
import ink.ckx.mo.admin.mapper.SysDictItemMapper
import ink.ckx.mo.admin.service.SysDictItemService
import org.springframework.stereotype.Service

/**
 * 数据字典项业务实现类
 *
 * @author chenkaixin
 */
@Service
class SysDictItemServiceImpl(
    private val dictItemConverter: DictItemConverter
) : ServiceImpl<SysDictItemMapper, SysDictItem>(), SysDictItemService {

    /**
     * 字典数据项分页列表
     *
     * @param dictItemPageQuery
     * @return
     */
    override fun listDictItemPages(dictItemPageQuery: DictItemPageQuery): Page<DictItemPageVO> {
        // 查询参数
        val pageNum = dictItemPageQuery.pageNum
        val pageSize = dictItemPageQuery.pageSize
        val keywords: String? = dictItemPageQuery.keywords
        val typeCode: String? = dictItemPageQuery.typeCode

        // 查询数据
        val dictItemPage = ktQuery()
            .select(SysDictItem::id, SysDictItem::name, SysDictItem::value, SysDictItem::status)
            .eq(StrUtil.isNotBlank(typeCode), SysDictItem::typeCode, typeCode)
            .like(StrUtil.isNotBlank(keywords), SysDictItem::name, keywords)
            .orderByDesc(SysDictItem::updateTime)
            .page(Page(pageNum, pageSize))

        // 实体转换
        return dictItemConverter.entity2Page(dictItemPage)
    }

    /**
     * 字典数据项表单详情
     *
     * @param itemId 字典数据项ID
     * @return
     */
    override fun getDictItemDetail(itemId: Long): DictItemDetailVO? {
        // 获取entity
        val entity = ktQuery()
            .select(
                SysDictItem::id, SysDictItem::typeCode, SysDictItem::name, SysDictItem::value,
                SysDictItem::status, SysDictItem::sort, SysDictItem::remark
            )
            .eq(SysDictItem::id, itemId)
            .one()
        // 实体转换
        return dictItemConverter.entity2DetailVO(entity)
    }

    /**
     * 新增字典数据项
     *
     * @param dictItemForm 字典数据项表单
     * @return
     */
    override fun saveDictItem(dictItemForm: DictItemForm): Long? {
        // 实体对象转换 form->entity
        val sysDictItem = dictItemConverter.form2Entity(dictItemForm)
        // 保存并返回数据项ID
        save(sysDictItem)
        return sysDictItem.id
    }

    /**
     * 修改字典数据项
     *
     * @param itemId           字典数据项ID
     * @param dictItemForm 字典数据项表单
     * @return
     */
    override fun updateDictItem(itemId: Long, dictItemForm: DictItemForm) {
        val sysDictItem = dictItemConverter.form2Entity(dictItemForm)
        sysDictItem.id = itemId
        updateById(sysDictItem)
    }

    /**
     * 删除字典数据项
     *
     * @param ids 字典数据项ID，多个以英文逗号(,)分割
     * @return
     */
    override fun deleteDictItems(ids: String) {
        val idList = ids.split(',').mapNotNull { it.toLongOrNull() }
        // 删除字典数据项
        if (idList.isNotEmpty()) {
            this.removeByIds(idList)
        }
    }
}