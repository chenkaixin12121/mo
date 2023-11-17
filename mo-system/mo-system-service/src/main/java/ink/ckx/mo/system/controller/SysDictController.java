package ink.ckx.mo.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.ckx.mo.common.core.result.Result;
import ink.ckx.mo.common.mybatis.result.PageResult;
import ink.ckx.mo.common.web.model.Option;
import ink.ckx.mo.system.api.model.form.DictItemForm;
import ink.ckx.mo.system.api.model.form.DictTypeForm;
import ink.ckx.mo.system.api.model.query.DictItemPageQuery;
import ink.ckx.mo.system.api.model.query.DictTypePageQuery;
import ink.ckx.mo.system.api.model.vo.dict.DictItemDetailVO;
import ink.ckx.mo.system.api.model.vo.dict.DictItemPageVO;
import ink.ckx.mo.system.api.model.vo.dict.DictTypeDetailVO;
import ink.ckx.mo.system.api.model.vo.dict.DictTypePageVO;
import ink.ckx.mo.system.service.SysDictItemService;
import ink.ckx.mo.system.service.SysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "字典接口")
@RestController
@RequestMapping("/api/v1/dict")
@RequiredArgsConstructor
public class SysDictController {

    private final SysDictItemService dictItemService;

    private final SysDictTypeService dictTypeService;

    @Operation(summary = "字典数据项分页列表")
    @PostMapping("/items/pages")
    public PageResult<DictItemPageVO> listDictItemPages(@RequestBody DictItemPageQuery dictItemPageQuery) {
        IPage<DictItemPageVO> result = dictItemService.listDictItemPages(dictItemPageQuery);
        return PageResult.success(result);
    }

    @Operation(summary = "获取字典数据项详情")
    @GetMapping("/items/{itemId}")
    public Result<DictItemDetailVO> getDictItemDetail(@Parameter(description = "字典数据项ID") @PathVariable Long itemId) {
        DictItemDetailVO dictItemDetailVO = dictItemService.getDictItemDetail(itemId);
        return Result.success(dictItemDetailVO);
    }

    @PreAuthorize("@pms.hasPerm('sys:dict:item:save')")
    @Operation(summary = "新增字典数据项")
    @PostMapping("/items")
    public Result<Long> saveDictItem(@Valid @RequestBody DictItemForm DictItemForm) {
        Long itemId = dictItemService.saveDictItem(DictItemForm);
        return Result.success(itemId);
    }

    @PreAuthorize("@pms.hasPerm('sys:dict:item:update')")
    @Operation(summary = "修改字典数据项")
    @PutMapping("/items/{itemId}")
    public Result<Void> updateDictItem(@Parameter(description = "字典数据项ID") @PathVariable Long itemId,
                                       @Valid @RequestBody DictItemForm DictItemForm) {
        dictItemService.updateDictItem(itemId, DictItemForm);
        return Result.success();
    }

    @PreAuthorize("@pms.hasPerm('sys:dict:item:delete')")
    @Operation(summary = "删除字典")
    @DeleteMapping("/items/{ids}")
    public Result<Void> deleteDictItems(@Parameter(description = "字典数据项ID，多个以英文逗号(,)分割") @PathVariable String ids) {
        dictItemService.deleteDictItems(ids);
        return Result.success();
    }

    @Operation(summary = "字典类型分页列表")
    @PostMapping("/types/pages")
    public PageResult<DictTypePageVO> listDictTypePages(@RequestBody DictTypePageQuery dictTypePageQuery) {
        Page<DictTypePageVO> result = dictTypeService.listDictTypePages(dictTypePageQuery);
        return PageResult.success(result);
    }

    @Operation(summary = "字典类型详情")
    @GetMapping("/types/{typeId}")
    public Result<DictTypeDetailVO> getDictTypeFormData(@Parameter(description = "字典类型ID") @PathVariable Long typeId) {
        DictTypeDetailVO dictTypeDetailVO = dictTypeService.getDictTypeFormData(typeId);
        return Result.success(dictTypeDetailVO);
    }

    @PreAuthorize("@pms.hasPerm('sys:dict:type:save')")
    @Operation(summary = "新增字典类型")
    @PostMapping("/types")
    public Result<Long> saveDictType(@Valid @RequestBody DictTypeForm dictTypeForm) {
        Long typeId = dictTypeService.saveDictType(dictTypeForm);
        return Result.success(typeId);
    }

    @PreAuthorize("@pms.hasPerm('sys:dict:type:update')")
    @Operation(summary = "修改字典类型")
    @PutMapping("/types/{typeId}")
    public Result<Void> updateDict(@Parameter(description = "字典类型ID") @PathVariable Long typeId,
                                   @RequestBody DictTypeForm dictTypeForm) {
        dictTypeService.updateDictType(typeId, dictTypeForm);
        return Result.success();
    }

    @PreAuthorize("@pms.hasPerm('sys:dict:type:delete')")
    @Operation(summary = "删除字典类型")
    @DeleteMapping("/types/{ids}")
    public Result<Void> deleteDictTypes(@Parameter(description = "字典类型ID，多个以英文逗号(,)分割") @PathVariable String ids) {
        dictTypeService.deleteDictTypes(ids);
        return Result.success();
    }

    @Operation(summary = "获取字典类型的数据项")
    @GetMapping("/types/{typeCode}/items")
    public Result<List<Option<String>>> listDictItemsByTypeCode(@Parameter(description = "字典类型编码") @PathVariable String typeCode) {
        List<Option<String>> optionList = dictTypeService.listDictItemsByTypeCode(typeCode);
        return Result.success(optionList);
    }
}