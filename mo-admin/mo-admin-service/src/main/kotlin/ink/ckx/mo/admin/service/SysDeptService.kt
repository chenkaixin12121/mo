package ink.ckx.mo.admin.service

import com.baomidou.mybatisplus.extension.service.IService
import ink.ckx.mo.admin.api.model.entity.SysDept
import ink.ckx.mo.admin.api.model.form.DeptForm
import ink.ckx.mo.admin.api.model.query.DeptListQuery
import ink.ckx.mo.admin.api.model.vo.dept.DeptDetailVO
import ink.ckx.mo.admin.api.model.vo.dept.DeptListVO
import ink.ckx.mo.common.web.model.Option

/**
 * 部门业务接口
 *
 * @author chenkaixin
 */
interface SysDeptService : IService<SysDept> {

    /**
     * 部门列表
     *
     * @return
     */
    fun listDepartments(deptListQuery: DeptListQuery): List<DeptListVO>

    /**
     * 获取部门详情
     *
     * @param deptId
     * @return
     */
    fun getDeptDetail(deptId: Long): DeptDetailVO?

    /**
     * 部门树形下拉选项
     *
     * @return
     */
    fun listDeptOptions(): List<Option<Long>>

    /**
     * 新增部门
     *
     * @param deptForm
     * @return
     */
    fun saveDept(deptForm: DeptForm): Long?

    /**
     * 修改部门
     *
     * @param deptId
     * @param deptForm
     * @return
     */
    fun updateDept(deptId: Long, deptForm: DeptForm)

    /**
     * 删除部门
     *
     * @param ids 部门ID，多个以英文逗号,拼接字符串
     * @return
     */
    fun deleteByIds(ids: String)
}