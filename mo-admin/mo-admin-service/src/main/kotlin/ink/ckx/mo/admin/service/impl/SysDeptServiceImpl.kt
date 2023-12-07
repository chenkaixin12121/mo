package ink.ckx.mo.admin.service.impl

import cn.hutool.core.bean.BeanUtil
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import ink.ckx.mo.admin.api.constant.AdminConstant
import ink.ckx.mo.admin.api.model.entity.SysDept
import ink.ckx.mo.admin.api.model.form.DeptForm
import ink.ckx.mo.admin.api.model.query.DeptListQuery
import ink.ckx.mo.admin.api.model.vo.dept.DeptDetailVO
import ink.ckx.mo.admin.api.model.vo.dept.DeptListVO
import ink.ckx.mo.admin.converter.DeptConverter
import ink.ckx.mo.admin.mapper.SysDeptMapper
import ink.ckx.mo.admin.service.SysDeptService
import ink.ckx.mo.common.web.enums.StatusEnum
import ink.ckx.mo.common.web.model.Option
import org.springframework.stereotype.Service

/**
 * 部门业务实现类
 *
 * @author chenkaixin
 */
@Service
class SysDeptServiceImpl(
    private val deptConverter: DeptConverter
) : ServiceImpl<SysDeptMapper, SysDept>(), SysDeptService {

    override fun listDepartments(deptListQuery: DeptListQuery): List<DeptListVO> {
        // 查询参数
        val keywords: String? = deptListQuery.keywords
        val status: StatusEnum? = deptListQuery.status

        // 查询数据
        val deptList = ktQuery()
            .select(
                SysDept::id, SysDept::parentId, SysDept::name, SysDept::status,
                SysDept::sort, SysDept::createTime, SysDept::updateTime
            )
            .eq(status != null, SysDept::status, status)
            .like(!keywords.isNullOrBlank(), SysDept::name, keywords)
            .orderByDesc(SysDept::sort, SysDept::updateTime)
            .list()
        val resultList: MutableList<DeptListVO> = mutableListOf()
        if (deptList.isNotEmpty()) {
            val cacheDeptIds = deptList.map { e -> e.id }.toMutableSet()
            for (dept in deptList) {
                val parentId = dept.parentId
                // 不在缓存ID列表的parentId是顶级节点ID，以此作为递归开始
                if (parentId !in cacheDeptIds) {
                    resultList.addAll(recurDepartments(parentId, deptList))
                    // 避免重复递归
                    cacheDeptIds.add(parentId)
                }
            }
        }

        //  列表为空说明所有的节点都是独立的
        return resultList.takeIf { it.isNotEmpty() } ?: deptList.map {
            val deptListVO = DeptListVO()
            BeanUtil.copyProperties(it, deptListVO)
            deptListVO
        }
    }

    /**
     * 递归生成部门层级列表
     *
     * @param parentId
     * @param deptList
     * @return
     */
    private fun recurDepartments(parentId: Long?, deptList: List<SysDept>): List<DeptListVO> {
        return deptList
            .filter { it.parentId == parentId }
            .map {
                val deptListVO = deptConverter.entity2VO(it)
                val children = recurDepartments(it.id, deptList)
                deptListVO.children = children
                deptListVO
            }
            .toList()
    }

    override fun getDeptDetail(deptId: Long): DeptDetailVO? {
        val entity = ktQuery()
            .select(SysDept::id, SysDept::name, SysDept::parentId, SysDept::status, SysDept::sort)
            .eq(SysDept::id, deptId)
            .one()
        return deptConverter.entity2DetailVO(entity)
    }

    override fun listDeptOptions(): List<Option<Long>> {
        val deptList = ktQuery()
            .select(SysDept::id, SysDept::parentId, SysDept::name)
            .eq(SysDept::status, StatusEnum.ENABLE)
            .orderByAsc(SysDept::sort)
            .list()
        return buildDeptTree(deptList)
    }

    override fun saveDept(deptForm: DeptForm): Long? {
        val sysDept = deptConverter.form2Entity(deptForm)
        // 部门路径
        val treePath = generateDeptTreePath(deptForm.parentId)
        sysDept.treePath = treePath
        // 保存部门并返回部门ID
        save(sysDept)
        return sysDept.id
    }

    override fun updateDept(deptId: Long, deptForm: DeptForm) {
        // form->entity
        val sysDept = deptConverter.form2Entity(deptForm)
        sysDept.id = deptId
        // 部门路径
        val treePath = generateDeptTreePath(deptForm.parentId)
        sysDept.treePath = treePath
        // 更新部门
        updateById(sysDept)
    }

    override fun deleteByIds(ids: String) {
        val idList = ids.split(',').mapNotNull { it.toLongOrNull() }
        if (idList.isNotEmpty()) {
            // 删除部门及子部门
            idList.forEach {
                ktUpdate()
                    .eq(SysDept::id, it)
                    .or()
                    .apply("concat (',',tree_path,',') like concat('%,',{0},',%')", it)
                    .remove()
            }
        }
    }

    /**
     * 递归生成部门表格层级列表
     *
     * @param deptList
     * @return
     */
    private fun buildDeptTree(deptList: List<SysDept>): List<Option<Long>> {
        if (deptList.isEmpty()) {
            return mutableListOf()
        }
        val returnList: MutableList<Option<Long>> = mutableListOf()
        val tempList = deptList.map(SysDept::id).toList()
        for (dept in deptList) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (dept.parentId !in tempList) {
                val option = Option(dept.id, dept.name)
                recursionFn(deptList, option)
                returnList.add(option)
            }
        }
        if (returnList.isEmpty()) {
            deptList.forEach {
                val option = Option(it.id, it.name)
                returnList.add(option)
            }
        }
        return returnList
    }

    /**
     * 递归列表
     */
    private fun recursionFn(list: List<SysDept>, t: Option<Long>) {
        // 得到子节点列表
        val childList = getChildList(list, t)
        t.children = childList
        for (tChild in childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild)
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private fun getChildList(list: List<SysDept>, t: Option<Long>): List<Option<Long>> {
        val childList: MutableList<Option<Long>> = mutableListOf()
        for (n in list) {
            if (n.parentId == t.value) {
                val option: Option<Long> = Option(n.id, n.name)
                childList.add(option)
            }
        }
        return childList
    }

    /**
     * 判断是否有子节点
     */
    private fun hasChild(list: List<SysDept>, t: Option<Long>): Boolean = getChildList(list, t).isNotEmpty()

    /**
     * 部门路径生成
     *
     * @param parentId
     * @return
     */
    private fun generateDeptTreePath(parentId: Long?): String {
        var treePath = ""
        if (AdminConstant.ROOT_ID == parentId) {
            treePath = parentId.toString() + ""
        } else {
            val parent = ktQuery()
                .select(SysDept::id, SysDept::treePath)
                .eq(SysDept::id, parentId)
                .one()
            if (parent != null) {
                treePath = parent.treePath + "," + parent.id
            }
        }
        return treePath
    }
}