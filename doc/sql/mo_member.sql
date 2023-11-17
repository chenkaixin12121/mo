/*
 Navicat Premium Data Transfer

 Source Server         : 本地-127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 127.0.0.1:3306
 Source Schema         : mo_member

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 23/11/2023 18:18:52
*/
USE mo_member;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for member_user
-- ----------------------------
DROP TABLE IF EXISTS `member_user`;
CREATE TABLE `member_user`  (
  `id` bigint NOT NULL COMMENT '主键',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `gender` tinyint(1) NULL DEFAULT NULL COMMENT '性别',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `openid` char(28) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'openid',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of member_user
-- ----------------------------
INSERT INTO `member_user` VALUES (1725045829309419522, '18312341234', '陈开心', 1, '2023-11-18', 'https://file.ckx.ink/upload/2020/04/android-chrome-512x512.png', '123', 1, 0, '2023-11-16 14:59:10', '2023-11-16 14:59:10');
INSERT INTO `member_user` VALUES (1725045829309419523, '18313331234', '陈开心333', 2, '2023-11-18', 'https://file.ckx.ink/upload/2020/04/android-chrome-512x512.png', '123', 2, 0, '2023-11-16 14:59:10', '2023-11-16 14:59:10');

SET FOREIGN_KEY_CHECKS = 1;
