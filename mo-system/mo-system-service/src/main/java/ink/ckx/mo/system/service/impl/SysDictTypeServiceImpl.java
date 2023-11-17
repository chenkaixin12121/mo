package ink.ckx.mo.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.ckx.mo.common.core.exception.BusinessException;
import ink.ckx.mo.common.core.result.ResultCode;
import ink.ckx.mo.common.web.model.Option;
import ink.ckx.mo.system.api.model.entity.SysDictItem;
import ink.ckx.mo.system.api.model.entity.SysDictType;
import ink.ckx.mo.system.api.model.form.DictTypeForm;
import ink.ckx.mo.system.api.model.query.DictTypePageQuery;
import ink.ckx.mo.system.api.model.vo.dict.DictTypeDetailVO;
import ink.ckx.mo.system.api.model.vo.dict.DictTypePageVO;
import ink.ckx.mo.system.converter.DictTypeConverter;
import ink.ckx.mo.system.mapper.SysDictTypeMapper;
import ink.ckx.mo.system.service.SysDictItemService;
import ink.ckx.mo.system.service.SysDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据字典类型业务实现类
 *
 * @author chenkaixin
 */
@Service
@RequiredArgsConstructor
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

    private final SysDictItemService dictItemService;

    private final DictTypeConverter dictTypeConverter;

    /**
     * 字典分页列表
     *
     * @param dictTypePageQuery 分页查询对象
     * @return
     */
    @Override
    public Page<DictTypePageVO> listDictTypePages(DictTypePageQuery dictTypePageQuery) {
        // 查询参数
        int pageNum = dictTypePageQuery.getPageNum();
        int pageSize = dictTypePageQuery.getPageSize();
        String keywords = dictTypePageQuery.getKeywords();

        // 查询数据
        Page<SysDictType> dictTypePage = this.page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<SysDictType>()
                        .select(SysDictType::getId, SysDictType::getName, SysDictType::getCode, SysDictType::getStatus)
                        .like(StrUtil.isNotBlank(keywords), SysDictType::getName, keywords)
                        .or()
                        .like(StrUtil.isNotBlank(keywords), SysDictType::getCode, keywords));

        // 实体转换
        return dictTypeConverter.entity2Page(dictTypePage);
    }

    /**
     * 获取字典类型表单详情
     *
     * @param id 字典类型ID
     * @return
     */
    @Override
    public DictTypeDetailVO getDictTypeFormData(Long id) {
        // 获取entity
        SysDictType entity = this.getOne(new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getId, id).select(SysDictType::getId, SysDictType::getName, SysDictType::getCode,
                        SysDictType::getStatus, SysDictType::getRemark));
        Assert.isFalse(entity == null, () -> new BusinessException(ResultCode.PARAM_ERROR, "字典类型不存在"));

        // 实体转换
        return dictTypeConverter.entity2DetailVO(entity);
    }

    /**
     * 新增字典类型
     *
     * @param dictTypeForm
     * @return
     */
    @Override
    public Long saveDictType(DictTypeForm dictTypeForm) {
        // 实体对象转换 form->entity
        SysDictType entity = dictTypeConverter.form2Entity(dictTypeForm);
        // 保存字典类型并返回类型ID
        this.save(entity);
        return entity.getId();
    }

    /**
     * 修改字典类型
     *
     * @param typeId       字典类型ID
     * @param dictTypeForm 字典类型表单
     * @return
     */
    @Override
    public void updateDictType(Long typeId, DictTypeForm dictTypeForm) {
        // 获取字典类型
        SysDictType sysDictType = this.getById(typeId);
        Assert.isFalse(sysDictType == null, () -> new BusinessException(ResultCode.PARAM_ERROR, "字典类型不存在"));

        SysDictType entity = dictTypeConverter.form2Entity(dictTypeForm);
        boolean result = this.updateById(entity);
        if (result) {
            // 字典类型code变化，同步修改字典项的类型code
            String oldCode = sysDictType.getCode();
            String newCode = dictTypeForm.getCode();
            if (!StrUtil.equals(oldCode, newCode)) {
                dictItemService.update(new LambdaUpdateWrapper<SysDictItem>()
                        .eq(SysDictItem::getTypeCode, oldCode).set(SysDictItem::getTypeCode, newCode));
            }
        }
    }

    /**
     * 删除字典类型
     *
     * @param ids 字典类型ID，多个以英文逗号(,)分割
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictTypes(String ids) {
        List<Long> idList = StrUtil.split(ids, ',', 0, true, Long::valueOf);
        if (CollUtil.isEmpty(idList)) {
            return;
        }
        // 删除字典数据项
        List<String> dictTypeCodes = this.list(new LambdaQueryWrapper<SysDictType>()
                        .select(SysDictType::getCode)
                        .in(SysDictType::getId, ids))
                .stream().map(SysDictType::getCode).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(dictTypeCodes)) {
            dictItemService.remove(new LambdaQueryWrapper<SysDictItem>().in(SysDictItem::getTypeCode, dictTypeCodes));
        }
        // 删除字典类型
        this.removeByIds(idList);
    }

    /**
     * 获取字典类型的数据项
     *
     * @param typeCode
     * @return
     */
    @Override
    public List<Option<String>> listDictItemsByTypeCode(String typeCode) {
        // 数据字典项
        List<SysDictItem> dictItems = dictItemService.list(new LambdaQueryWrapper<SysDictItem>()
                .select(SysDictItem::getValue, SysDictItem::getName)
                .eq(SysDictItem::getTypeCode, typeCode));

        // 转换下拉数据
        return CollectionUtil.emptyIfNull(dictItems).stream()
                .map(dictItem -> new Option<>(dictItem.getValue(), dictItem.getName())).collect(Collectors.toList());
    }
}