package ink.ckx.mo.common.mybatis.handler

import cn.hutool.core.util.StrUtil
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils
import com.baomidou.mybatisplus.core.toolkit.StringPool
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler
import ink.ckx.mo.common.core.base.IBaseEnum.Companion.getEnumByValue
import ink.ckx.mo.common.mybatis.annotation.DataPermission
import ink.ckx.mo.common.mybatis.enums.DataScopeEnum
import ink.ckx.mo.common.security.util.getDataScope
import ink.ckx.mo.common.security.util.getDeptId
import ink.ckx.mo.common.security.util.getUserId
import ink.ckx.mo.common.security.util.isSuperAdmin
import net.sf.jsqlparser.expression.Expression
import net.sf.jsqlparser.expression.operators.conditional.AndExpression
import net.sf.jsqlparser.parser.CCJSqlParserUtil

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/21
 */
class MyDataPermissionHandler : DataPermissionHandler {

    override fun getSqlSegment(where: Expression, mappedStatementId: String): Expression {
        val clazz = Class.forName(mappedStatementId.substring(0, mappedStatementId.lastIndexOf(StringPool.DOT)))
        val methodName = mappedStatementId.substring(mappedStatementId.lastIndexOf(StringPool.DOT) + 1)
        val methods = clazz.declaredMethods
        for (method in methods) {
            val annotation = method.getAnnotation(DataPermission::class.java) ?: return where
            // 超级管理员不受数据权限控制
            if (isSuperAdmin()) {
                return where
            }
            if (ObjectUtils.isNotEmpty(annotation) && (method.name == methodName || method.name + "_COUNT" == methodName)) {
                return dataScopeFilter(
                    annotation.deptAlias,
                    annotation.deptIdColumnName,
                    annotation.userAlias,
                    annotation.userIdColumnName,
                    where
                )
            }
        }
        return where
    }

    companion object {

        /**
         * 构建过滤条件
         *
         * @param where 当前查询条件
         * @return 构建后查询条件
         */
        fun dataScopeFilter(
            deptAlias: String,
            deptIdColumnName: String,
            userAlias: String,
            userIdColumnName: String,
            where: Expression
        ): Expression {

            val deptColumnName =
                if (deptAlias.isNotBlank()) deptAlias + StringPool.DOT + deptIdColumnName else deptIdColumnName
            val userColumnName =
                if (userAlias.isNotBlank()) userAlias + StringPool.DOT + userIdColumnName else userIdColumnName

            // 获取当前用户的数据权限
            val dataScope = getDataScope()
            val dataScopeEnum = getEnumByValue(dataScope, DataScopeEnum::class.java)
            val deptId: Int
            val userId: Int
            val appendSqlStr: String

            when (dataScopeEnum) {
                DataScopeEnum.ALL -> return where
                DataScopeEnum.DEPT -> {
                    deptId = getDeptId()
                    appendSqlStr = deptColumnName + StringPool.EQUALS + deptId
                }

                DataScopeEnum.SELF -> {
                    userId = getUserId()
                    appendSqlStr = userColumnName + StringPool.EQUALS + userId
                }

                else -> {
                    deptId = getDeptId()
                    appendSqlStr =
                        "$deptColumnName IN ( SELECT id FROM sys_dept WHERE id = $deptId or find_in_set( $deptId , tree_path ) )"
                }
            }
            if (StrUtil.isBlank(appendSqlStr)) {
                return where
            }
            val appendExpression = CCJSqlParserUtil.parseCondExpression(appendSqlStr)
            return AndExpression(where, appendExpression)
        }
    }
}