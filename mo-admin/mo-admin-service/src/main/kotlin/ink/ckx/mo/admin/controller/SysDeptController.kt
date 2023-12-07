package ink.ckx.mo.admin.controller

import ink.ckx.mo.admin.api.model.form.DeptForm
import ink.ckx.mo.admin.api.model.query.DeptListQuery
import ink.ckx.mo.admin.api.model.vo.dept.DeptDetailVO
import ink.ckx.mo.admin.api.model.vo.dept.DeptListVO
import ink.ckx.mo.admin.service.SysDeptService
import ink.ckx.mo.common.core.result.Result
import ink.ckx.mo.common.core.result.Result.Companion.success
import ink.ckx.mo.common.web.model.Option
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * 部门控制器
 *
 * @author chenkaixin
 */
@Tag(name = "部门接口")
@RestController
@RequestMapping("/api/v1/dept")
class SysDeptController(
    private val deptService: SysDeptService
) {

    @Operation(summary = "获取部门列表")
    @PostMapping("/list")
    fun listDepartments(@RequestBody deptListQuery: DeptListQuery): Result<List<DeptListVO>> {
        val deptListVOList = deptService.listDepartments(deptListQuery)
        return success(deptListVOList)
    }

    @Operation(summary = "获取部门详情")
    @GetMapping("/{deptId}")
    fun getDeptDetail(@Parameter(description = "部门ID") @PathVariable deptId: Long): Result<DeptDetailVO?> {
        val deptDetailVO = deptService.getDeptDetail(deptId)
        return success(deptDetailVO)
    }

    @GetMapping("/options")
    fun listDeptOptions(): Result<List<Option<Long>>> {
        val result = deptService.listDeptOptions()
        return success(result)
    }

    @PreAuthorize("@pms.hasPerm('sys:dept:save')")
    @Operation(summary = "新增部门")
    @PostMapping
    fun saveDept(@RequestBody @Valid deptForm: DeptForm): Result<Long?> {
        val deptId = deptService.saveDept(deptForm)
        return success(deptId)
    }

    @PreAuthorize("@pms.hasPerm('sys:dept:update')")
    @Operation(summary = "修改部门")
    @PutMapping(value = ["/{deptId}"])
    fun updateDept(
        @Parameter(description = "部门ID") @PathVariable deptId: Long,
        @RequestBody @Valid deptForm: DeptForm
    ): Result<Void?> {
        deptService.updateDept(deptId, deptForm)
        return success()
    }

    @PreAuthorize("@pms.hasPerm('sys:dept:delete')")
    @Operation(summary = "删除部门")
    @DeleteMapping("/{ids}")
    fun deleteDepartments(@Parameter(description = "部门ID，多个以英文逗号(,)分割") @PathVariable ids: String): Result<Void?> {
        deptService.deleteByIds(ids)
        return success()
    }
}