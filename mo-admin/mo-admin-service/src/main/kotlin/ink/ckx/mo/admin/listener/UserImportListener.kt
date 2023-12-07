package ink.ckx.mo.admin.listener

import cn.hutool.core.lang.Validator
import cn.hutool.extra.spring.SpringUtil
import cn.hutool.json.JSONUtil
import com.alibaba.excel.context.AnalysisContext
import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper
import ink.ckx.mo.admin.api.model.entity.SysUser
import ink.ckx.mo.admin.api.model.entity.SysUserRole
import ink.ckx.mo.admin.api.model.vo.user.UserImportVO
import ink.ckx.mo.admin.converter.UserConverter
import ink.ckx.mo.admin.service.SysUserRoleService
import ink.ckx.mo.admin.service.SysUserService
import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.web.enums.GenderEnum
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/23
 */
class UserImportListener(

    // 部门ID
    private val deptId: Long,

    // 角色ID
    private val roleIds: String

) : MyAnalysisEventListener<UserImportVO>() {

    private val log = KotlinLogging.logger {}

    private val userService: SysUserService = SpringUtil.getBean(SysUserService::class.java)

    private val passwordEncoder: PasswordEncoder = SpringUtil.getBean(PasswordEncoder::class.java)

    private val userConverter: UserConverter = SpringUtil.getBean(UserConverter::class.java)

    private val userRoleService: SysUserRoleService = SpringUtil.getBean(SysUserRoleService::class.java)

    // 导入返回信息
    override var msg = StringBuilder()

    // 有效条数
    private var validCount = 0

    // 无效条数
    private var invalidCount = 0

    fun getMsg(): String {
        return "导入用户结束：成功${validCount}条，失败${invalidCount}条；<br/>${msg}"
    }

    override operator fun invoke(userImportVO: UserImportVO, context: AnalysisContext) {
        log.info { "解析到一条用户数据: ${JSONUtil.toJsonStr(userImportVO)}" }
        val validationMsg = StringBuilder()
        // 校验数据
        val username: String? = userImportVO.username
        if (username.isNullOrBlank()) {
            validationMsg.append("用户名为空；")
        } else {
            val count = KtQueryChainWrapper(SysUser()).eq(SysUser::username, username).count()
            if (count > 0) {
                validationMsg.append("用户名已存在；")
            }
        }
        val nickname: String? = userImportVO.nickname
        if (nickname.isNullOrBlank()) {
            validationMsg.append("用户昵称为空；")
        }
        val gender: String? = userImportVO.gender
        if (gender.isNullOrBlank()) {
            validationMsg.append("性别为空；")
        } else {
            val validGender = GenderEnum.values().none { it.label == gender }
            if (validGender) {
                validationMsg.append("性别不正确；")
            }
        }
        val mobile: String? = userImportVO.mobile
        if (mobile.isNullOrBlank()) {
            validationMsg.append("手机号码为空；")
        } else {
            if (!Validator.isMobile(mobile)) {
                validationMsg.append("手机号码不正确；")
            }
        }
        val email: String? = userImportVO.email
        if (email.isNullOrBlank()) {
            validationMsg.append("邮箱为空；")
        } else {
            if (!Validator.isEmail(email)) {
                validationMsg.append("邮箱不正确；")
            }
        }
        if (validationMsg.isBlank()) {
            // 校验通过，持久化至数据库
            val entity = userConverter.importVO2Entity(userImportVO)
            entity.deptId = deptId
            entity.password = passwordEncoder.encode(CoreConstant.DEFAULT_USER_PASSWORD)
            val saveResult = userService.save(entity)
            if (saveResult) {
                validCount++
                // 保存用户角色关联
                val roleIdList = roleIds.split(',').mapNotNull { it.toLongOrNull() }
                if (roleIdList.isNotEmpty()) {
                    val userRoles = roleIdList.map { SysUserRole(null, entity.id, it) }.toList()
                    userRoleService.saveBatch(userRoles)
                }
            } else {
                invalidCount++
                msg.append("第").append(validCount + invalidCount).append("行数据保存失败；<br/>")
            }
        } else {
            invalidCount++
            msg.append("第").append(validCount + invalidCount).append("行数据校验失败：").append(validationMsg)
                .append("<br/>")
        }
    }

    override fun doAfterAllAnalysed(analysisContext: AnalysisContext) {}
}