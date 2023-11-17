package ink.ckx.mo.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.ckx.mo.common.core.exception.BusinessException;
import ink.ckx.mo.common.core.result.ResultCode;
import ink.ckx.mo.system.api.model.entity.SysDictItem;
import ink.ckx.mo.system.api.model.form.DictItemForm;
import ink.ckx.mo.system.api.model.query.DictItemPageQuery;
import ink.ckx.mo.system.api.model.vo.dict.DictItemDetailVO;
import ink.ckx.mo.system.api.model.vo.dict.DictItemPageVO;
import ink.ckx.mo.system.converter.DictItemConverter;
import ink.ckx.mo.system.mapper.SysDictItemMapper;
import ink.ckx.mo.system.service.SysDictItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据字典项业务实现类
 *
 * @author chenkaixin
 */
@Service
@RequiredArgsConstructor
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements SysDictItemService {

    private final DictItemConverter dictItemConverter;

    /**
     * 字典数据项分页列表
     *
     * @param dictItemPageQuery
     * @return
     */
    @Override
    public Page<DictItemPageVO> listDictItemPages(DictItemPageQuery dictItemPageQuery) {
        // 查询参数
        int pageNum = dictItemPageQuery.getPageNum();
        int pageSize = dictItemPageQuery.getPageSize();
        String keywords = dictItemPageQuery.getKeywords();
        String typeCode = dictItemPageQuery.getTypeCode();

        // 查询数据
        Page<SysDictItem> dictItemPage = this.page(new Page<>(pageNum, pageSize), new LambdaQueryWrapper<SysDictItem>()
                .select(SysDictItem::getId, SysDictItem::getName, SysDictItem::getValue, SysDictItem::getStatus)
                .eq(StrUtil.isNotBlank(typeCode), SysDictItem::getTypeCode, typeCode)
                .like(StrUtil.isNotBlank(keywords), SysDictItem::getName, keywords));

        // 实体转换
        return dictItemConverter.entity2Page(dictItemPage);
    }

    /**
     * 字典数据项表单详情
     *
     * @param itemId 字典数据项ID
     * @return
     */
    @Override
    public DictItemDetailVO getDictItemDetail(Long itemId) {
        // 获取entity
        SysDictItem entity = this.getOne(new LambdaQueryWrapper<SysDictItem>()
                .eq(SysDictItem::getId, itemId)
                .select(SysDictItem::getId, SysDictItem::getTypeCode, SysDictItem::getName, SysDictItem::getValue,
                        SysDictItem::getStatus, SysDictItem::getSort, SysDictItem::getRemark));
        Assert.isFalse(entity == null, () -> new BusinessException(ResultCode.PARAM_ERROR, "字典数据项不存在"));

        // 实体转换
        return dictItemConverter.entity2DetailVO(entity);
    }

    /**
     * 新增字典数据项
     *
     * @param dictItemForm 字典数据项表单
     * @return
     */
    @Override
    public Long saveDictItem(DictItemForm dictItemForm) {
        // 实体对象转换 form->entity
        SysDictItem sysDictItem = dictItemConverter.form2Entity(dictItemForm);
        // 保存并返回数据项ID
        this.save(sysDictItem);
        return sysDictItem.getId();
    }

    /**
     * 修改字典数据项
     *
     * @param id           字典数据项ID
     * @param dictItemForm 字典数据项表单
     * @return
     */
    @Override
    public void updateDictItem(Long id, DictItemForm dictItemForm) {
        SysDictItem sysDictItem = dictItemConverter.form2Entity(dictItemForm);
        sysDictItem.setId(id);
        this.updateById(sysDictItem);
    }

    /**
     * 删除字典数据项
     *
     * @param idsStr 字典数据项ID，多个以英文逗号(,)分割
     * @return
     */
    @Override
    public void deleteDictItems(String idsStr) {
        List<Long> idList = CharSequenceUtil.split(idsStr, ',', 0, true, Long::valueOf);
        // 删除字典数据项
        if (CollUtil.isNotEmpty(idList)) {
            this.removeByIds(idList);
        }
    }
}