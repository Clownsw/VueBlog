/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : localhost:3306
 Source Schema         : vueblog

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 05/04/2022 20:08:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for m_blog
-- ----------------------------
DROP TABLE IF EXISTS `m_blog`;
CREATE TABLE `m_blog`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `created` datetime(0) NOT NULL,
  `status` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_blog
-- ----------------------------
INSERT INTO `m_blog` VALUES (1, 1, 'test1', 'test1', 'test1', '2022-03-31 13:49:07', 1);
INSERT INTO `m_blog` VALUES (3, 1, 'test3', 'test3', 'test3', '2022-03-31 07:14:12', 0);
INSERT INTO `m_blog` VALUES (4, 2, 'test2', 'test2', 'test2', '2022-03-31 07:14:42', 0);
INSERT INTO `m_blog` VALUES (5, 1, 'test5', 'test5', 'test5', '2022-03-31 10:19:42', 0);
INSERT INTO `m_blog` VALUES (6, 1, 'test6', 'test6', 'test6', '2022-04-01 09:15:49', 1);
INSERT INTO `m_blog` VALUES (7, 1, 'test7', 'test7', 'test7', '2022-04-01 09:15:59', 1);
INSERT INTO `m_blog` VALUES (8, 2, 'test8', 'test8', 'test8', '2022-04-01 09:16:13', 1);
INSERT INTO `m_blog` VALUES (9, 1, '这是一个测试文章', '这是一个测试文章', '# 这是一个测试内容', '2022-04-01 12:17:37', 0);
INSERT INTO `m_blog` VALUES (10, 1, '测试文章2', '测试文章2', '测试文章2', '2022-04-01 12:21:40', 0);
INSERT INTO `m_blog` VALUES (11, 1, '测试文章3', '测试文章3', '测试文章3', '2022-04-01 12:24:08', 0);
INSERT INTO `m_blog` VALUES (12, 1, '测试文章4', '测试文章4', '测试文章4', '2022-04-01 12:25:06', 0);
INSERT INTO `m_blog` VALUES (13, 1, '测试文章5', '测试文章5', '测试文章5', '2022-04-01 12:25:29', 0);
INSERT INTO `m_blog` VALUES (14, 1, '测试md', '测试md', '# Hello\n## 你好\n### Hi\n#### 嗨', '2022-04-01 12:26:18', 0);

-- ----------------------------
-- Table structure for m_friend
-- ----------------------------
DROP TABLE IF EXISTS `m_friend`;
CREATE TABLE `m_friend`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `href` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_friend
-- ----------------------------

-- ----------------------------
-- Table structure for m_user
-- ----------------------------
DROP TABLE IF EXISTS `m_user`;
CREATE TABLE `m_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(5) NOT NULL,
  `created` datetime(0) NOT NULL,
  `last_login` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_user
-- ----------------------------
INSERT INTO `m_user` VALUES (1, 'xiaoxiao', 'https://avatars.githubusercontent.com/u/28394742', 'msmliexx1@gmail.com', '123123', 1, '2022-03-29 21:23:55', '2022-03-29 21:24:00');
INSERT INTO `m_user` VALUES (2, 'x', 'https://avatars.githubusercontent.com/u/28394742', 'msmliexx1@gmail.com', '123123', 0, '2022-03-29 21:23:55', '2022-03-29 21:24:00');

SET FOREIGN_KEY_CHECKS = 1;
