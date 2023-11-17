package ink.ckx.mo.system.controller;

import ink.ckx.mo.common.core.result.Result;
import ink.ckx.mo.common.web.model.Option;
import ink.ckx.mo.system.api.model.form.DeptForm;
import ink.ckx.mo.system.api.model.query.DeptListQuery;
import ink.ckx.mo.system.api.model.vo.dept.DeptDetailVO;
import ink.ckx.mo.system.api.model.vo.dept.DeptListVO;
import ink.ckx.mo.system.service.SysDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门控制器
 *
 * @author chenkaixin
 */
@Tag(name = "部门接口")
@RestController
@RequestMapping("/api/v1/dept")
@RequiredArgsConstructor
public class SysDeptController {

    private final SysDeptService deptService;

    @Operation(summary = "获取部门列表")
    @PostMapping("/list")
    public Result<List<DeptListVO>> listDepartments(@RequestBody DeptListQuery deptListQuery) {
        List<DeptListVO> deptListVOList = deptService.listDepartments(deptListQuery);
        return Result.success(deptListVOList);
    }

    @Operation(summary = "获取部门详情")
    @GetMapping("/{deptId}")
    public Result<DeptDetailVO> getDeptDetail(@Parameter(description = "部门ID") @PathVariable Long deptId) {
        DeptDetailVO deptDetailVO = deptService.getDeptDetail(deptId);
        return Result.success(deptDetailVO);
    }

    @GetMapping("/options")
    public Result<List<Option<Long>>> listDeptOptions() {
        List<Option<Long>> result = deptService.listDeptOptions();
        return Result.success(result);
    }

    @PreAuthorize("@pms.hasPerm('sys:dept:save')")
    @Operation(summary = "新增部门")
    @PostMapping
    public Result<Long> saveDept(@Valid @RequestBody DeptForm deptForm) {
        Long deptId = deptService.saveDept(deptForm);
        return Result.success(deptId);
    }

    @PreAuthorize("@pms.hasPerm('sys:dept:update')")
    @Operation(summary = "修改部门")
    @PutMapping(value = "/{deptId}")
    public Result<Void> updateDept(@Parameter(description = "部门ID") @PathVariable Long deptId,
                                   @Valid @RequestBody DeptForm deptForm) {
        deptService.updateDept(deptId, deptForm);
        return Result.success();
    }

    @PreAuthorize("@pms.hasPerm('sys:dept:delete')")
    @Operation(summary = "删除部门")
    @DeleteMapping("/{ids}")
    public Result<Void> deleteDepartments(@Parameter(description = "部门ID，多个以英文逗号(,)分割") @PathVariable String ids) {
        deptService.deleteByIds(ids);
        return Result.success();
    }
}