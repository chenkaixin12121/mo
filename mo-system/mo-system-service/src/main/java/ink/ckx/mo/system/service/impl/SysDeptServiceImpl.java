package ink.ckx.mo.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.ckx.mo.common.web.enums.StatusEnum;
import ink.ckx.mo.common.web.model.Option;
import ink.ckx.mo.system.api.constant.SystemConstant;
import ink.ckx.mo.system.api.model.entity.SysDept;
import ink.ckx.mo.system.api.model.form.DeptForm;
import ink.ckx.mo.system.api.model.query.DeptListQuery;
import ink.ckx.mo.system.api.model.vo.dept.DeptDetailVO;
import ink.ckx.mo.system.api.model.vo.dept.DeptListVO;
import ink.ckx.mo.system.converter.DeptConverter;
import ink.ckx.mo.system.mapper.SysDeptMapper;
import ink.ckx.mo.system.service.SysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 部门业务实现类
 *
 * @author chenkaixin
 */
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    private final DeptConverter deptConverter;

    @Override
    public List<DeptListVO> listDepartments(DeptListQuery deptListQuery) {
        // 查询参数
        String keywords = deptListQuery.getKeywords();
        StatusEnum status = deptListQuery.getStatus();

        // 查询数据
        List<SysDept> deptList = this.list(new LambdaQueryWrapper<SysDept>()
                .select(SysDept::getId, SysDept::getParentId, SysDept::getName, SysDept::getStatus,
                        SysDept::getSort, SysDept::getCreateTime, SysDept::getUpdateTime)
                .eq(status != null, SysDept::getStatus, status)
                .like(StrUtil.isNotBlank(keywords), SysDept::getName, keywords)
                .orderByAsc(SysDept::getSort));

        List<DeptListVO> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(deptList)) {
            Set<Long> cacheDeptIds = deptList.stream().map(SysDept::getId).collect(Collectors.toSet());
            for (SysDept dept : deptList) {
                Long parentId = dept.getParentId();
                // 不在缓存ID列表的parentId是顶级节点ID，以此作为递归开始
                if (!cacheDeptIds.contains(parentId)) {
                    list.addAll(this.recurDepartments(parentId, deptList));
                    // 避免重复递归
                    cacheDeptIds.add(parentId);
                }
            }
        }

        //  列表为空说明所有的节点都是独立的
        if (list.isEmpty()) {
            return deptList.stream().map(item -> {
                DeptListVO deptListVO = new DeptListVO();
                BeanUtil.copyProperties(item, deptListVO);
                return deptListVO;
            }).collect(Collectors.toList());
        }

        return list;
    }

    /**
     * 递归生成部门层级列表
     *
     * @param parentId
     * @param deptList
     * @return
     */
    private List<DeptListVO> recurDepartments(Long parentId, List<SysDept> deptList) {
        return deptList.stream().filter(dept -> dept.getParentId().equals(parentId)).map(dept -> {
            DeptListVO deptListVO = deptConverter.entity2VO(dept);
            List<DeptListVO> children = this.recurDepartments(dept.getId(), deptList);
            deptListVO.setChildren(children);
            return deptListVO;
        }).toList();
    }

    @Override
    public DeptDetailVO getDeptDetail(Long deptId) {
        SysDept entity = this.getOne(new LambdaQueryWrapper<SysDept>()
                .select(SysDept::getId, SysDept::getName, SysDept::getParentId, SysDept::getStatus, SysDept::getSort)
                .eq(SysDept::getId, deptId));

        return deptConverter.entity2DetailVO(entity);
    }

    @Override
    public List<Option<Long>> listDeptOptions() {
        List<SysDept> deptList = this.list(new LambdaQueryWrapper<SysDept>()
                .select(SysDept::getId, SysDept::getParentId, SysDept::getName)
                .eq(SysDept::getStatus, StatusEnum.ENABLE)
                .orderByAsc(SysDept::getSort));

        //   List<Option> options = recurDeptTreeOptions(SystemConstants.ROOT_NODE_ID, deptList);
        return this.buildDeptTree(deptList);
    }

    @Override
    public Long saveDept(DeptForm deptForm) {
        SysDept sysDept = deptConverter.form2Entity(deptForm);
        // 部门路径
        String treePath = this.generateDeptTreePath(deptForm.getParentId());
        sysDept.setTreePath(treePath);
        // 保存部门并返回部门ID
        this.save(sysDept);
        return sysDept.getId();
    }

    @Override
    public void updateDept(Long deptId, DeptForm formData) {
        // form->entity
        SysDept sysDept = deptConverter.form2Entity(formData);
        sysDept.setId(deptId);
        // 部门路径
        String treePath = this.generateDeptTreePath(formData.getParentId());
        sysDept.setTreePath(treePath);
        // 更新部门
        this.updateById(sysDept);
    }

    @Override
    public void deleteByIds(String ids) {
        List<Long> idList = StrUtil.split(ids, ',', 0, true, Long::valueOf);
        if (CollUtil.isNotEmpty(idList)) {
            // 删除部门及子部门
            idList.forEach(deptId ->
                    this.remove(new LambdaQueryWrapper<SysDept>()
                            .eq(SysDept::getId, deptId)
                            .or()
                            .apply("concat (',',tree_path,',') like concat('%,',{0},',%')", deptId)));
        }
    }

    /**
     * 递归生成部门表格层级列表
     *
     * @param deptList
     * @return
     */
    private List<Option<Long>> buildDeptTree(List<SysDept> deptList) {
        if (CollUtil.isEmpty(deptList)) {
            return new ArrayList<>();
        }
        List<Option<Long>> returnList = new ArrayList<>();
        List<Long> tempList = deptList.stream().map(SysDept::getId).toList();
        for (SysDept dept : deptList) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                Option<Long> option = new Option<>(dept.getId(), dept.getName());
                this.recursionFn(deptList, option);
                returnList.add(option);
            }
        }
        if (CollUtil.isEmpty(returnList)) {
            deptList.forEach(dept -> {
                Option<Long> option = new Option<>(dept.getId(), dept.getName());
                returnList.add(option);
            });
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, Option<Long> t) {
        // 得到子节点列表
        List<Option<Long>> childList = getChildList(list, t);
        t.setChildren(childList);
        for (Option<Long> tChild : childList) {
            if (this.hasChild(list, tChild)) {
                this.recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<Option<Long>> getChildList(List<SysDept> list, Option<Long> t) {
        List<Option<Long>> tlist = new ArrayList<>();
        for (SysDept n : list) {
            if (n.getParentId() != null && Objects.equals(n.getParentId(), t.getValue())) {
                Option<Long> option = new Option<>(n.getId(), n.getName());
                tlist.add(option);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, Option<Long> t) {
        return this.getChildList(list, t).size() > 0;
    }

    /**
     * 部门路径生成
     *
     * @param parentId
     * @return
     */
    private String generateDeptTreePath(Long parentId) {
        String treePath = null;
        if (SystemConstant.ROOT_ID.equals(parentId)) {
            treePath = parentId + "";
        } else {
            SysDept parent = this.getOne(new LambdaQueryWrapper<SysDept>()
                    .select(SysDept::getId, SysDept::getTreePath)
                    .eq(SysDept::getId, parentId));
            if (parent != null) {
                treePath = parent.getTreePath() + "," + parent.getId();
            }
        }
        return treePath;
    }
}