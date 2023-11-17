package ink.ckx.mo.system.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.web.enums.GenderEnum;
import ink.ckx.mo.system.api.model.entity.SysUser;
import ink.ckx.mo.system.api.model.entity.SysUserRole;
import ink.ckx.mo.system.api.model.vo.user.UserImportVO;
import ink.ckx.mo.system.converter.UserConverter;
import ink.ckx.mo.system.service.SysUserRoleService;
import ink.ckx.mo.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/23
 */
@Slf4j
public class UserImportListener extends MyAnalysisEventListener<UserImportVO> {

    // 部门ID
    private final Long deptId;
    // 角色ID
    private final String roleIds;
    private final SysUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserConverter userConverter;
    private final SysUserRoleService userRoleService;
    // 导入返回信息
    StringBuilder msg = new StringBuilder();
    // 有效条数
    private int validCount;
    // 无效条数
    private int invalidCount;

    public UserImportListener(Long deptId, String roleIds) {
        this.deptId = deptId;
        this.roleIds = roleIds;
        this.userService = SpringUtil.getBean(SysUserService.class);
        this.passwordEncoder = SpringUtil.getBean(PasswordEncoder.class);
        this.userRoleService = SpringUtil.getBean(SysUserRoleService.class);
        this.userConverter = SpringUtil.getBean(UserConverter.class);
    }

    @Override
    public String getMsg() {
        return StrUtil.format("导入用户结束：成功{}条，失败{}条；<br/>{}", validCount, invalidCount, msg);
    }

    @Override
    public void invoke(UserImportVO userImportVO, AnalysisContext analysisContext) {
        log.info("解析到一条用户数据:{}", JSONUtil.toJsonStr(userImportVO));
        // 校验数据
        StringBuilder validationMsg = new StringBuilder();

        String username = userImportVO.getUsername();
        if (StrUtil.isBlank(username)) {
            validationMsg.append("用户名为空；");
        } else {
            long count = userService.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
            if (count > 0) {
                validationMsg.append("用户名已存在；");
            }
        }

        String nickname = userImportVO.getNickname();
        if (StrUtil.isBlank(nickname)) {
            validationMsg.append("用户昵称为空；");
        }

        String gender = userImportVO.getGender();
        if (StrUtil.isBlank(gender)) {
            validationMsg.append("性别为空；");
        } else {
            boolean validGender = Arrays.stream(GenderEnum.values()).noneMatch(e -> e.getLabel().equals(gender));
            if (validGender) {
                validationMsg.append("性别不正确；");
            }
        }

        String mobile = userImportVO.getMobile();
        if (StrUtil.isBlank(mobile)) {
            validationMsg.append("手机号码为空；");
        } else {
            if (!Validator.isMobile(mobile)) {
                validationMsg.append("手机号码不正确；");
            }
        }

        String email = userImportVO.getEmail();
        if (StrUtil.isBlank(email)) {
            validationMsg.append("邮箱为空；");
        } else {
            if (!Validator.isEmail(email)) {
                validationMsg.append("邮箱不正确；");
            }
        }

        if (validationMsg.length() == 0) {
            // 校验通过，持久化至数据库
            SysUser entity = userConverter.importVO2Entity(userImportVO);
            entity.setDeptId(deptId);
            entity.setPassword(passwordEncoder.encode(CoreConstant.DEFAULT_USER_PASSWORD));

            boolean saveResult = userService.save(entity);
            if (saveResult) {
                validCount++;
                // 保存用户角色关联
                List<Long> roleIdList = CharSequenceUtil.split(roleIds, ',', 0, true, Long::valueOf);
                if (CollUtil.isNotEmpty(roleIdList)) {
                    List<SysUserRole> userRoles = roleIdList.stream()
                            .map(roleId -> new SysUserRole(null, entity.getId(), roleId))
                            .collect(Collectors.toList());
                    userRoleService.saveBatch(userRoles);
                }
            } else {
                invalidCount++;
                msg.append("第").append(validCount + invalidCount).append("行数据保存失败；<br/>");
            }
        } else {
            invalidCount++;
            msg.append("第").append(validCount + invalidCount).append("行数据校验失败：").append(validationMsg).append("<br/>");
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }
}
