package ink.ckx.mo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.ckx.mo.common.mybatis.annotation.DataPermission;
import ink.ckx.mo.system.api.model.dto.SysUserInfoDTO;
import ink.ckx.mo.system.api.model.entity.SysUser;
import ink.ckx.mo.system.api.model.query.UserPageQuery;
import ink.ckx.mo.system.api.model.vo.user.UserDetailVO;
import ink.ckx.mo.system.api.model.vo.user.UserExportVO;
import ink.ckx.mo.system.api.model.vo.user.UserPageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户持久层
 *
 * @author chenkaixin
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 获取用户分页列表
     *
     * @param page
     * @param queryParams 查询参数
     * @return
     */
    @DataPermission(deptAlias = "u")
    Page<UserPageVO> listUserPages(Page<UserPageVO> page, UserPageQuery queryParams);

    /**
     * 获取用户表单详情
     *
     * @param userId 用户ID
     * @return
     */
    UserDetailVO getUserDetail(Long userId);

    /**
     * 根据用户名获取认证信息
     *
     * @param username
     * @return
     */
    SysUserInfoDTO getUserAuthInfo(String username);

    /**
     * 获取导出用户列表
     *
     * @param queryParams
     * @return
     */
    @DataPermission(deptAlias = "u")
    List<UserExportVO> listExportUsers(UserPageQuery queryParams);
}