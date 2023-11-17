package ink.ckx.mo.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import ink.ckx.mo.system.api.model.entity.SysDictItem;
import ink.ckx.mo.system.api.model.form.DictItemForm;
import ink.ckx.mo.system.api.model.query.DictItemPageQuery;
import ink.ckx.mo.system.api.model.vo.dict.DictItemDetailVO;
import ink.ckx.mo.system.api.model.vo.dict.DictItemPageVO;

/**
 *
 */
public interface SysDictItemService extends IService<SysDictItem> {

    /**
     * 字典数据项分页列表
     *
     * @param dictItemPageQuery
     * @return
     */
    Page<DictItemPageVO> listDictItemPages(DictItemPageQuery dictItemPageQuery);

    /**
     * 字典数据项表单详情
     *
     * @param itemId 字典数据项ID
     * @return
     */
    DictItemDetailVO getDictItemDetail(Long itemId);

    /**
     * 新增字典数据项
     *
     * @param dictItemForm 字典数据项表单
     * @return
     */
    Long saveDictItem(DictItemForm dictItemForm);

    /**
     * 修改字典数据项
     *
     * @param itemId       字典数据项ID
     * @param dictItemForm 字典数据项表单
     * @return
     */
    void updateDictItem(Long itemId, DictItemForm dictItemForm);

    /**
     * 删除字典数据项
     *
     * @param ids 字典数据项ID，多个以英文逗号(,)分割
     * @return
     */
    void deleteDictItems(String ids);
}