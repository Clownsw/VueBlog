/*
 Navicat Premium Data Transfer

 Source Server         : mysql8-3333
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3333
 Source Schema         : vueblog_admin

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 27/08/2022 13:33:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_blog
-- ----------------------------
DROP TABLE IF EXISTS `sys_blog`;
CREATE TABLE `sys_blog`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `sort_id` int NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `created` datetime NOT NULL,
  `status` tinyint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  FULLTEXT INDEX `VUEBLOG_M_BLOG_TITLE_DESCRIPTION_CONTENT_FULL_TEXT`(`title`, `description`, `content`)
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_blog
-- ----------------------------
INSERT INTO `sys_blog` VALUES (57, 1, 1, '1', '1', '1', '2022-08-27 11:11:23', 0);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `code` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `state` int NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------

-- ----------------------------
-- Table structure for sys_res
-- ----------------------------
DROP TABLE IF EXISTS `sys_res`;
CREATE TABLE `sys_res`  (
  `id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `parent_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `permission` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `path` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `del` int NOT NULL DEFAULT 1,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_res
-- ----------------------------
INSERT INTO `sys_res` VALUES ('1', NULL, '/', '/', '/', 0, '2020-02-09 00:00:00');
INSERT INTO `sys_res` VALUES ('2', NULL, 'dashboard', 'dashboard', 'dashboard', 0, '2020-02-09 00:00:00');
INSERT INTO `sys_res` VALUES ('206267260095041511', NULL, '首页', '/', '', 0, '2020-08-13 11:43:26');
INSERT INTO `sys_res` VALUES ('3', NULL, 'form', 'form', 'form', 0, '2020-02-09 00:00:00');
INSERT INTO `sys_res` VALUES ('4', NULL, 'table', 'table', 'table', 0, '2020-02-09 00:00:00');
INSERT INTO `sys_res` VALUES ('5', NULL, 'profile', 'profile', 'profile', 0, '2020-02-09 00:00:00');
INSERT INTO `sys_res` VALUES ('6', NULL, 'result', 'result', 'result', 0, '2020-02-09 00:00:00');
INSERT INTO `sys_res` VALUES ('7', NULL, 'exception', 'exception', 'exception', 0, '2020-02-09 00:00:00');
INSERT INTO `sys_res` VALUES ('8', NULL, 'user', 'user', 'user', 0, '2020-02-09 00:00:00');
INSERT INTO `sys_res` VALUES ('9', NULL, 'setting', 'setting', 'setting', 0, '2020-02-09 00:00:00');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `del` int NOT NULL DEFAULT 1,
  `create_date` datetime NOT NULL,
  `parent_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', 'super', 0, '2020-07-28 08:34:40', NULL);

-- ----------------------------
-- Table structure for sys_role_res
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_res`;
CREATE TABLE `sys_role_res`  (
  `id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `res_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_res
-- ----------------------------
INSERT INTO `sys_role_res` VALUES ('1', '1', '1', '2020-07-28 08:34:40');
INSERT INTO `sys_role_res` VALUES ('10', '1', '9', '2020-07-28 08:34:40');
INSERT INTO `sys_role_res` VALUES ('2', '1', '206267260095041511', '2020-07-28 08:34:40');
INSERT INTO `sys_role_res` VALUES ('3', '1', '2', '2020-07-28 08:34:40');
INSERT INTO `sys_role_res` VALUES ('4', '1', '3', '2020-07-28 08:34:40');
INSERT INTO `sys_role_res` VALUES ('5', '1', '4', '2020-07-28 08:34:40');
INSERT INTO `sys_role_res` VALUES ('6', '1', '5', '2020-07-28 08:34:40');
INSERT INTO `sys_role_res` VALUES ('7', '1', '6', '2020-07-28 08:34:40');
INSERT INTO `sys_role_res` VALUES ('8', '1', '7', '2020-07-28 08:34:40');
INSERT INTO `sys_role_res` VALUES ('9', '1', '8', '2020-07-28 08:34:40');

-- ----------------------------
-- Table structure for sys_trash
-- ----------------------------
DROP TABLE IF EXISTS `sys_trash`;
CREATE TABLE `sys_trash`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `table_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `create_date` datetime NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_trash
-- ----------------------------
INSERT INTO `sys_trash` VALUES ('6309ac6300c95c84008f8b74', 'sys_user', '{\"id\":\"270466997106642944\",\"account\":\"15625285826\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"name\":\"johnson\",\"login_check\":null,\"state\":1,\"del\":0,\"create_date\":\"2021-08-18 16:19:17\"}', '2022-08-27 13:32:20');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `account` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `login_check` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'PasswordQRCodeCheck',
  `del` int NOT NULL DEFAULT 1,
  `create_date` datetime NOT NULL,
  `state` int NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('205667537625681919', '00000000000', '4297f44b13955235245b2497399d7a93', '超级管理员', 'PasswordCheck', 0, '2020-07-28 08:34:40', 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('6309ac7500c95c84008f8b75', '205667537625681919', '1', '2022-08-27 13:32:37');

SET FOREIGN_KEY_CHECKS = 1;
