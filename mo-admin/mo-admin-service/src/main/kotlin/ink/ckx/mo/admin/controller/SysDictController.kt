package ink.ckx.mo.admin.controller

import com.baomidou.mybatisplus.core.metadata.IPage
import ink.ckx.mo.admin.api.model.form.DictItemForm
import ink.ckx.mo.admin.api.model.form.DictTypeForm
import ink.ckx.mo.admin.api.model.query.DictItemPageQuery
import ink.ckx.mo.admin.api.model.query.DictTypePageQuery
import ink.ckx.mo.admin.api.model.vo.dict.DictItemDetailVO
import ink.ckx.mo.admin.api.model.vo.dict.DictItemPageVO
import ink.ckx.mo.admin.api.model.vo.dict.DictTypeDetailVO
import ink.ckx.mo.admin.api.model.vo.dict.DictTypePageVO
import ink.ckx.mo.admin.service.SysDictItemService
import ink.ckx.mo.admin.service.SysDictTypeService
import ink.ckx.mo.common.core.result.Result
import ink.ckx.mo.common.core.result.Result.Companion.success
import ink.ckx.mo.common.mybatis.result.PageResult
import ink.ckx.mo.common.web.model.Option
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Tag(name = "字典接口")
@RestController
@RequestMapping("/api/v1/dict")
class SysDictController(
    private val dictItemService: SysDictItemService,
    private val dictTypeService: SysDictTypeService,
) {

    @Operation(summary = "字典数据项分页列表")
    @PostMapping("/items/pages")
    fun listDictItemPages(@RequestBody dictItemPageQuery: DictItemPageQuery): PageResult<DictItemPageVO> {
        val result: IPage<DictItemPageVO> = dictItemService.listDictItemPages(dictItemPageQuery)
        return PageResult.success(result)
    }

    @Operation(summary = "获取字典数据项详情")
    @GetMapping("/items/{itemId}")
    fun getDictItemDetail(@Parameter(description = "字典数据项ID") @PathVariable itemId: Long): Result<DictItemDetailVO?> {
        val dictItemDetailVO = dictItemService.getDictItemDetail(itemId)
        return success(dictItemDetailVO)
    }

    @PreAuthorize("@pms.hasPerm('sys:dict:item:save')")
    @Operation(summary = "新增字典数据项")
    @PostMapping("/items")
    fun saveDictItem(@RequestBody @Valid DictItemForm: DictItemForm): Result<Long?> {
        val itemId = dictItemService.saveDictItem(DictItemForm)
        return success(itemId)
    }

    @PreAuthorize("@pms.hasPerm('sys:dict:item:update')")
    @Operation(summary = "修改字典数据项")
    @PutMapping("/items/{itemId}")
    fun updateDictItem(
        @Parameter(description = "字典数据项ID") @PathVariable itemId: Long,
        @RequestBody @Valid DictItemForm: DictItemForm
    ): Result<Void?> {
        dictItemService.updateDictItem(itemId, DictItemForm)
        return success()
    }

    @PreAuthorize("@pms.hasPerm('sys:dict:item:delete')")
    @Operation(summary = "删除字典")
    @DeleteMapping("/items/{ids}")
    fun deleteDictItems(@Parameter(description = "字典数据项ID，多个以英文逗号(,)分割") @PathVariable ids: String): Result<Void?> {
        dictItemService.deleteDictItems(ids)
        return success()
    }

    @Operation(summary = "字典类型分页列表")
    @PostMapping("/types/pages")
    fun listDictTypePages(@RequestBody dictTypePageQuery: DictTypePageQuery): PageResult<DictTypePageVO> {
        val result = dictTypeService.listDictTypePages(dictTypePageQuery)
        return PageResult.success(result)
    }

    @Operation(summary = "字典类型详情")
    @GetMapping("/types/{typeId}")
    fun getDictTypeFormData(@Parameter(description = "字典类型ID") @PathVariable typeId: Long): Result<DictTypeDetailVO?> {
        val dictTypeDetailVO = dictTypeService.getDictTypeFormData(typeId)
        return success(dictTypeDetailVO)
    }

    @PreAuthorize("@pms.hasPerm('sys:dict:type:save')")
    @Operation(summary = "新增字典类型")
    @PostMapping("/types")
    fun saveDictType(@RequestBody @Valid dictTypeForm: DictTypeForm): Result<Long?> {
        val typeId = dictTypeService.saveDictType(dictTypeForm)
        return success(typeId)
    }

    @PreAuthorize("@pms.hasPerm('sys:dict:type:update')")
    @Operation(summary = "修改字典类型")
    @PutMapping("/types/{typeId}")
    fun updateDict(
        @Parameter(description = "字典类型ID") @PathVariable typeId: Long,
        @RequestBody dictTypeForm: DictTypeForm
    ): Result<Void?> {
        dictTypeService.updateDictType(typeId, dictTypeForm)
        return success()
    }

    @PreAuthorize("@pms.hasPerm('sys:dict:type:delete')")
    @Operation(summary = "删除字典类型")
    @DeleteMapping("/types/{ids}")
    fun deleteDictTypes(@Parameter(description = "字典类型ID，多个以英文逗号(,)分割") @PathVariable ids: String): Result<Void?> {
        dictTypeService.deleteDictTypes(ids)
        return success()
    }

    @Operation(summary = "获取字典类型的数据项")
    @GetMapping("/types/{typeCode}/items")
    fun listDictItemsByTypeCode(@Parameter(description = "字典类型编码") @PathVariable typeCode: String): Result<List<Option<String>>> {
        val optionList = dictTypeService.listDictItemsByTypeCode(typeCode)
        return success(optionList)
    }
}