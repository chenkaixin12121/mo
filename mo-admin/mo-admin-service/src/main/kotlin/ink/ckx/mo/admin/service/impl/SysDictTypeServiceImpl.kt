package ink.ckx.mo.admin.service.impl

import cn.hutool.core.lang.Assert
import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateChainWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import ink.ckx.mo.admin.api.model.entity.SysDictItem
import ink.ckx.mo.admin.api.model.entity.SysDictType
import ink.ckx.mo.admin.api.model.form.DictTypeForm
import ink.ckx.mo.admin.api.model.query.DictTypePageQuery
import ink.ckx.mo.admin.api.model.vo.dict.DictTypeDetailVO
import ink.ckx.mo.admin.api.model.vo.dict.DictTypePageVO
import ink.ckx.mo.admin.converter.DictTypeConverter
import ink.ckx.mo.admin.mapper.SysDictTypeMapper
import ink.ckx.mo.admin.service.SysDictTypeService
import ink.ckx.mo.common.core.exception.BusinessException
import ink.ckx.mo.common.core.result.ResultCode
import ink.ckx.mo.common.web.model.Option
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 数据字典类型业务实现类
 *
 * @author chenkaixin
 */
@Service
class SysDictTypeServiceImpl(
    private val dictTypeConverter: DictTypeConverter,
) : ServiceImpl<SysDictTypeMapper, SysDictType>(), SysDictTypeService {

    /**
     * 字典分页列表
     *
     * @param dictTypePageQuery 分页查询对象
     * @return
     */
    override fun listDictTypePages(dictTypePageQuery: DictTypePageQuery): Page<DictTypePageVO> {
        // 查询参数
        val pageNum = dictTypePageQuery.pageNum
        val pageSize = dictTypePageQuery.pageSize
        val keywords: String? = dictTypePageQuery.keywords

        // 查询数据
        val dictTypePage = ktQuery()
            .select(SysDictType::id, SysDictType::name, SysDictType::code, SysDictType::status)
            .like(!keywords.isNullOrBlank(), SysDictType::name, keywords)
            .or()
            .like(!keywords.isNullOrBlank(), SysDictType::code, keywords)
            .orderByDesc(SysDictType::updateTime)
            .page(Page(pageNum, pageSize))

        // 实体转换
        return dictTypeConverter.entity2Page(dictTypePage)
    }

    /**
     * 获取字典类型表单详情
     *
     * @param typeId 字典类型ID
     * @return
     */
    override fun getDictTypeFormData(typeId: Long): DictTypeDetailVO? {
        // 获取entity
        val entity = ktQuery()
            .select(
                SysDictType::id, SysDictType::name, SysDictType::code,
                SysDictType::status, SysDictType::remark
            )
            .eq(SysDictType::id, typeId)
            .one()
        // 实体转换
        return dictTypeConverter.entity2DetailVO(entity)
    }

    /**
     * 新增字典类型
     *
     * @param dictTypeForm
     * @return
     */
    override fun saveDictType(dictTypeForm: DictTypeForm): Long? {
        // 实体对象转换 form->entity
        val entity = dictTypeConverter.form2Entity(dictTypeForm)
        // 保存字典类型并返回类型ID
        save(entity)
        return entity.id
    }

    /**
     * 修改字典类型
     *
     * @param typeId       字典类型ID
     * @param dictTypeForm 字典类型表单
     * @return
     */
    override fun updateDictType(typeId: Long, dictTypeForm: DictTypeForm) {
        // 获取字典类型
        val sysDictType = getById(typeId)
        Assert.isFalse(sysDictType == null) { BusinessException(ResultCode.PARAM_ERROR, "字典类型不存在") }
        val entity = dictTypeConverter.form2Entity(dictTypeForm)
        entity.id = typeId
        val result = updateById(entity)
        if (result) {
            // 字典类型code变化，同步修改字典项的类型code
            val oldCode = sysDictType.code
            val newCode = dictTypeForm.code
            if (oldCode != newCode) {
                KtUpdateChainWrapper(SysDictItem())
                    .eq(SysDictItem::typeCode, oldCode)
                    .set(SysDictItem::typeCode, newCode)
                    .update()
            }
        }
    }

    /**
     * 删除字典类型
     *
     * @param ids 字典类型ID，多个以英文逗号(,)分割
     * @return
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deleteDictTypes(ids: String) {
        val idList = ids.split(',').mapNotNull { it.toLongOrNull() }
        if (idList.isEmpty()) {
            return
        }
        // 删除字典数据项
        val dictTypeCodes = ktQuery()
            .select(SysDictType::code)
            .`in`(SysDictType::id, idList)
            .list()
            .map(SysDictType::code)
            .toList()
        if (dictTypeCodes.isNotEmpty()) {
            KtUpdateChainWrapper(SysDictItem())
                .`in`(SysDictItem::typeCode, dictTypeCodes)
                .remove()
        }
        // 删除字典类型
        this.removeByIds(idList)
    }

    /**
     * 获取字典类型的数据项
     *
     * @param typeCode
     * @return
     */
    override fun listDictItemsByTypeCode(typeCode: String): List<Option<String>> {
        // 数据字典项
        val dictItems = KtQueryChainWrapper(SysDictItem::class.java)
            .select(SysDictItem::value, SysDictItem::name)
            .eq(SysDictItem::typeCode, typeCode)
            .list()

        // 转换下拉数据
        return dictItems.map { Option(it.value, it.name) }.toList()
    }
}