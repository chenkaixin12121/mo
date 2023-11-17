package ink.ckx.mo.system.api.model.bo;

import ink.ckx.mo.system.api.enums.MenuTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * 路由BO
 */
@Data
public class RouteBO {

    /**
     * 菜单id
     */
    private Long id;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单类型(1-菜单；2-按钮)
     */
    private MenuTypeEnum type;

    /**
     * 路由路径(浏览器地址栏路径)
     */
    private String path;

    /**
     * 组件路径(vue页面完整路径，省略.vue后缀)
     */
    private String component;

    /**
     * 权限标识
     */
    private String perm;

    /**
     * 显示状态
     */
    private Integer visible;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 如果设置为 true，目录没有子节点也会显示
     */
    private Boolean alwaysShow;

    /**
     * 拥有路由的权限
     */
    private List<String> roles;
}