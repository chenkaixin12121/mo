/*
 Navicat Premium Data Transfer

 Source Server         : 本地-127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 127.0.0.1:3306
 Source Schema         : mo_admin

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 21/12/2023 16:22:50
*/
USE mo_admin;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '部门名称',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父节点id',
  `tree_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '父节点id路径',
  `sort` int NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态(1:正常;0:禁用)',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标识(1:已删除;0:未删除)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, 'xx科技', 0, '0', 3, 1, 0, '2023-11-15 14:22:47', '2023-11-23 15:23:56');
INSERT INTO `sys_dept` VALUES (2, '研发部门', 1, '0,1', 1, 1, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');
INSERT INTO `sys_dept` VALUES (3, '测试部门', 1, '0,1', 1, 1, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item`  (
  `id` bigint NOT NULL COMMENT '主键',
  `type_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典项名称',
  `value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典项值',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态(1:正常;0:禁用)',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `defaulted` tinyint NOT NULL DEFAULT 0 COMMENT '是否默认(1:是;0:否)',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标识(1:已删除;0:未删除)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES (1, 'gender', '男', '1', 1, 1, 0, NULL, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');
INSERT INTO `sys_dict_item` VALUES (2, 'gender', '女', '2', 1, 2, 0, NULL, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');
INSERT INTO `sys_dict_item` VALUES (3, 'gender', '未知', '0', 1, 1, 0, NULL, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');
INSERT INTO `sys_dict_item` VALUES (7, 'micro_service', '授权中心', 'mo-auth', 1, 1, 0, NULL, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');
INSERT INTO `sys_dict_item` VALUES (8, 'micro_service', '系统服务', 'mo-admin', 1, 2, 0, NULL, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');
INSERT INTO `sys_dict_item` VALUES (9, 'micro_service', '会员服务', 'mo-member', 1, 3, 0, NULL, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');
INSERT INTO `sys_dict_item` VALUES (10, 'micro_service', '支付中心', 'mo-pay', 1, 4, 0, NULL, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');
INSERT INTO `sys_dict_item` VALUES (16, 'request_method', '不限', '*', 1, 1, 0, NULL, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');
INSERT INTO `sys_dict_item` VALUES (17, 'request_method', 'GET', 'GET', 1, 2, 0, NULL, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');
INSERT INTO `sys_dict_item` VALUES (18, 'request_method', 'POST', 'POST', 1, 3, 0, NULL, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');
INSERT INTO `sys_dict_item` VALUES (19, 'request_method', 'PUT', 'PUT', 1, 4, 0, NULL, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');
INSERT INTO `sys_dict_item` VALUES (20, 'request_method', 'DELETE', 'DELETE', 1, 5, 0, NULL, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');
INSERT INTO `sys_dict_item` VALUES (21, 'request_method', 'PATCH', 'PATCH', 1, 6, 0, NULL, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` bigint NOT NULL COMMENT '主键 ',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '类型名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '类型编码',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态(0:正常;1:禁用)',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标识(1:已删除;0:未删除)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `type_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '性别', 'gender', 1, NULL, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');
INSERT INTO `sys_dict_type` VALUES (2, '微服务列表', 'micro_service', 1, NULL, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');
INSERT INTO `sys_dict_type` VALUES (3, '请求方式', 'request_method', 1, NULL, 0, '2023-11-15 14:22:47', '2023-11-15 14:22:47');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NOT NULL COMMENT '父菜单ID',
  `tree_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '父节点ID路径',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单名称',
  `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单图标',
  `type` tinyint NOT NULL COMMENT '菜单类型(1:目录；2:菜单；3:按钮)',
  `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由路径(浏览器地址栏路径)',
  `component` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径(vue页面完整路径，省略.vue后缀)',
  `perm` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `visible` tinyint(1) NOT NULL DEFAULT 1 COMMENT '显示状态(1-显示;0-隐藏)',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `always_show` tinyint NOT NULL DEFAULT 0 COMMENT '始终显示',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1737506478710976514 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '0', '系统管理', 'system', 1, '/system', 'Layout', NULL, 1, 1, 1, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (2, 1, '0,1', '用户管理', 'user', 2, 'user', 'system/user/index', NULL, 1, 1, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (3, 1, '0,1', '角色管理', 'role', 2, 'role', 'system/role/index', NULL, 1, 2, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (4, 1, '0,1', '菜单管理', 'menu', 2, 'menu', 'system/menu/index', NULL, 1, 3, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (5, 1, '0,1', '部门管理', 'tree', 2, 'dept', 'system/dept/index', NULL, 1, 4, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (6, 1, '0,1', '字典管理', 'dict', 2, 'dict', 'system/dict/index', NULL, 1, 5, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (31, 2, '0,1,2', '用户新增', '', 3, '', NULL, 'sys:user:save', 1, 1, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (32, 2, '0,1,2', '用户编辑', '', 3, '', NULL, 'sys:user:update', 1, 2, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (33, 2, '0,1,2', '用户删除', '', 3, '', NULL, 'sys:user:delete', 1, 3, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (70, 3, '0,1,3', '角色新增', '', 3, '', NULL, 'sys:role:save', 1, 1, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (71, 3, '0,1,3', '角色编辑', '', 3, '', NULL, 'sys:role:update', 1, 2, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (72, 3, '0,1,3', '角色删除', '', 3, '', NULL, 'sys:role:delete', 1, 3, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (73, 4, '0,1,4', '菜单新增', '', 3, '', NULL, 'sys:menu:save', 1, 1, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (74, 4, '0,1,4', '菜单编辑', '', 3, '', NULL, 'sys:menu:update', 1, 3, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (75, 4, '0,1,4', '菜单删除', '', 3, '', NULL, 'sys:menu:delete', 1, 3, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (76, 5, '0,1,5', '部门新增', '', 3, '', NULL, 'sys:dept:save', 1, 1, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (77, 5, '0,1,5', '部门编辑', '', 3, '', NULL, 'sys:dept:update', 1, 2, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (78, 5, '0,1,5', '部门删除', '', 3, '', NULL, 'sys:dept:delete', 1, 3, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (79, 6, '0,1,6', '字典类型新增', '', 3, '', NULL, 'sys:dict:type:save', 1, 1, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (81, 6, '0,1,6', '字典类型编辑', '', 3, '', NULL, 'sys:dict:type:update', 1, 2, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (84, 6, '0,1,6', '字典类型删除', '', 3, '', NULL, 'sys:dict:type:delete', 1, 3, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (85, 6, '0,1,6', '字典数据新增', '', 3, '', NULL, 'sys:dict:item:save', 1, 4, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (86, 6, '0,1,6', '字典数据编辑', '', 3, '', NULL, 'sys:dict:item:update', 1, 5, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (87, 6, '0,1,6', '字典数据删除', '', 3, '', NULL, 'sys:dict:item:delete', 1, 6, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (88, 2, '0,1,2', '重置密码', '', 3, '', NULL, 'sys:user:update:password', 1, 4, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (100, 0, '0', '会员管理', 'user', 1, '/member', 'Layout', NULL, 1, 4, 1, 0, '2023-11-17 09:18:44', '2023-11-18 02:17:00');
INSERT INTO `sys_menu` VALUES (101, 100, '0,100', '会员列表', 'peoples', 2, 'member', 'member/user/index', NULL, 1, 1, 0, 0, '2023-11-17 09:18:44', '2023-11-17 09:18:44');
INSERT INTO `sys_menu` VALUES (1727593013343014914, 2, '0,1727593013343014914,1727593013343014914', '导入用户', '', 3, '', NULL, 'sys:user:_import', 1, 4, 0, 0, '2023-11-23 15:40:46', '2023-11-23 15:51:06');
INSERT INTO `sys_menu` VALUES (1727595558291460098, 2, '0,1,2,1727595558291460098', '导出用户', '', 3, '', NULL, 'sys:user:_export', 1, 5, 0, 0, '2023-11-23 15:50:53', '2023-11-23 15:51:11');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码',
  `sort` int NOT NULL COMMENT '显示顺序',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '角色状态(1-正常；0-停用)',
  `data_scope` tinyint NOT NULL COMMENT '数据权限',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标识(0-未删除；1-已删除)',
  `create_time` datetime NOT NULL COMMENT '更新时间',
  `update_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'SUPER-ADMIN', 1, 1, 0, 0, '2023-11-15 14:56:51', '2023-11-15 14:56:51');
INSERT INTO `sys_role` VALUES (2, '系统管理员', 'ADMIN', 1, 1, 0, 0, '2023-11-15 14:56:51', '2023-11-15 14:56:51');
INSERT INTO `sys_role` VALUES (3, '访问游客', 'GUEST', 3, 1, 10, 0, '2023-11-15 14:56:51', '2023-11-21 15:02:03');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint NOT NULL COMMENT '主键',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1727607820502728705, 2, 1);
INSERT INTO `sys_role_menu` VALUES (1727607820502728706, 2, 2);
INSERT INTO `sys_role_menu` VALUES (1727607820502728707, 2, 31);
INSERT INTO `sys_role_menu` VALUES (1727607820502728708, 2, 32);
INSERT INTO `sys_role_menu` VALUES (1727607820502728709, 2, 33);
INSERT INTO `sys_role_menu` VALUES (1727607820502728710, 2, 88);
INSERT INTO `sys_role_menu` VALUES (1727607820502728711, 2, 1727593013343014914);
INSERT INTO `sys_role_menu` VALUES (1727607820502728712, 2, 1727595558291460098);
INSERT INTO `sys_role_menu` VALUES (1727607820502728713, 2, 3);
INSERT INTO `sys_role_menu` VALUES (1727607820502728714, 2, 70);
INSERT INTO `sys_role_menu` VALUES (1727607820502728715, 2, 71);
INSERT INTO `sys_role_menu` VALUES (1727607820502728716, 2, 72);
INSERT INTO `sys_role_menu` VALUES (1727607820502728717, 2, 4);
INSERT INTO `sys_role_menu` VALUES (1727607820502728718, 2, 73);
INSERT INTO `sys_role_menu` VALUES (1727607820502728719, 2, 74);
INSERT INTO `sys_role_menu` VALUES (1727607820502728720, 2, 75);
INSERT INTO `sys_role_menu` VALUES (1727607820502728721, 2, 5);
INSERT INTO `sys_role_menu` VALUES (1727607820502728722, 2, 76);
INSERT INTO `sys_role_menu` VALUES (1727607820502728723, 2, 77);
INSERT INTO `sys_role_menu` VALUES (1727607820502728724, 2, 78);
INSERT INTO `sys_role_menu` VALUES (1727607820502728725, 2, 6);
INSERT INTO `sys_role_menu` VALUES (1727607820502728726, 2, 79);
INSERT INTO `sys_role_menu` VALUES (1727607820502728727, 2, 81);
INSERT INTO `sys_role_menu` VALUES (1727607820502728728, 2, 84);
INSERT INTO `sys_role_menu` VALUES (1727607820502728729, 2, 85);
INSERT INTO `sys_role_menu` VALUES (1727607820502728730, 2, 86);
INSERT INTO `sys_role_menu` VALUES (1727607820502728731, 2, 87);
INSERT INTO `sys_role_menu` VALUES (1727607820502728732, 2, 100);
INSERT INTO `sys_role_menu` VALUES (1727607820502728733, 2, 101);
INSERT INTO `sys_role_menu` VALUES (1727627868265033730, 3, 1);
INSERT INTO `sys_role_menu` VALUES (1727627868265033731, 3, 2);
INSERT INTO `sys_role_menu` VALUES (1727627868265033732, 3, 33);
INSERT INTO `sys_role_menu` VALUES (1727627868265033733, 3, 3);
INSERT INTO `sys_role_menu` VALUES (1727627868265033734, 3, 72);
INSERT INTO `sys_role_menu` VALUES (1727627868265033735, 3, 4);
INSERT INTO `sys_role_menu` VALUES (1727627868265033736, 3, 75);
INSERT INTO `sys_role_menu` VALUES (1727627868265033737, 3, 5);
INSERT INTO `sys_role_menu` VALUES (1727627868265033738, 3, 78);
INSERT INTO `sys_role_menu` VALUES (1727627868265033739, 3, 6);
INSERT INTO `sys_role_menu` VALUES (1727627868265033740, 3, 84);
INSERT INTO `sys_role_menu` VALUES (1727627868265033741, 3, 87);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL COMMENT '主键',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `gender` tinyint(1) NULL DEFAULT 1 COMMENT '性别((1:男;2:女))',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `dept_id` int NULL DEFAULT NULL COMMENT '部门ID',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户头像',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '用户状态((1:正常;0:禁用))',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识(0:未删除;1:已删除)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `login_name`(`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'root', '超级管理员', 0, '$2a$10$BEMKNROPITz2Jm2a4myL4uAA776wilSPiKAb8dBIP/lLcglukQeW2', 1, 'https://blog.ckx.ink/upload/2020/04/android-chrome-512x512.png', '18348388576', 'chenkaixin12121@163.com', 1, 0, '2023-11-15 01:31:29', '2023-11-15 01:31:29');
INSERT INTO `sys_user` VALUES (2, 'admin', '系统管理员', 1, '$2a$10$BEMKNROPITz2Jm2a4myL4uAA776wilSPiKAb8dBIP/lLcglukQeW2', 2, 'https://blog.ckx.ink/upload/2020/04/android-chrome-512x512.png', '18348388576', '', 1, 0, '2023-11-15 01:31:29', '2023-11-15 01:31:29');
INSERT INTO `sys_user` VALUES (3, 'test', '测试小用户', 2, '$2a$10$.V285U.inSazxaWuTBhERetyCzGWUSWG8tHPEImDm02W8a5hWD6ua', 3, 'https://blog.ckx.ink/upload/2020/04/android-chrome-512x512.png', '18348388576', 'chenkaixin12121@163.com', 1, 0, '2023-11-15 01:31:29', '2023-11-15 01:31:29');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint NOT NULL COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2, 2);
INSERT INTO `sys_user_role` VALUES (3, 3, 3);

SET FOREIGN_KEY_CHECKS = 1;
