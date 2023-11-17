package ink.ckx.mo.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ink.ckx.mo.common.web.model.Option;
import ink.ckx.mo.system.api.model.entity.SysDept;
import ink.ckx.mo.system.api.model.form.DeptForm;
import ink.ckx.mo.system.api.model.query.DeptListQuery;
import ink.ckx.mo.system.api.model.vo.dept.DeptDetailVO;
import ink.ckx.mo.system.api.model.vo.dept.DeptListVO;

import java.util.List;

/**
 * 部门业务接口
 *
 * @author chenkaixin
 */
public interface SysDeptService extends IService<SysDept> {

    /**
     * 部门列表
     *
     * @return
     */
    List<DeptListVO> listDepartments(DeptListQuery deptListQuery);

    /**
     * 获取部门详情
     *
     * @param deptId
     * @return
     */
    DeptDetailVO getDeptDetail(Long deptId);

    /**
     * 部门树形下拉选项
     *
     * @return
     */
    List<Option<Long>> listDeptOptions();

    /**
     * 新增部门
     *
     * @param deptForm
     * @return
     */
    Long saveDept(DeptForm deptForm);

    /**
     * 修改部门
     *
     * @param deptId
     * @param deptForm
     * @return
     */
    void updateDept(Long deptId, DeptForm deptForm);

    /**
     * 删除部门
     *
     * @param ids 部门ID，多个以英文逗号,拼接字符串
     * @return
     */
    void deleteByIds(String ids);
}