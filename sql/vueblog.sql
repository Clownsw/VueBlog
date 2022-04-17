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

 Date: 17/04/2022 20:11:32
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
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_blog
-- ----------------------------
INSERT INTO `m_blog` VALUES (1, 1, 'test1', 'test1', 'Hello World', '2022-03-31 13:49:07', 1);
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
INSERT INTO `m_blog` VALUES (15, 1, 'Hello,World', 'Hello,World', 'Hello,World', '2022-04-17 10:55:09', 0);
INSERT INTO `m_blog` VALUES (16, 1, '1', '1', '1', '2022-04-17 11:23:00', 0);

-- ----------------------------
-- Table structure for m_blogtag
-- ----------------------------
DROP TABLE IF EXISTS `m_blogtag`;
CREATE TABLE `m_blogtag`  (
  `blogId` bigint(20) NOT NULL,
  `tagId` bigint(20) NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_blogtag
-- ----------------------------
INSERT INTO `m_blogtag` VALUES (14, 1);
INSERT INTO `m_blogtag` VALUES (14, 2);
INSERT INTO `m_blogtag` VALUES (14, 13);
INSERT INTO `m_blogtag` VALUES (14, 12);
INSERT INTO `m_blogtag` VALUES (14, 14);
INSERT INTO `m_blogtag` VALUES (13, 15);
INSERT INTO `m_blogtag` VALUES (13, 16);
INSERT INTO `m_blogtag` VALUES (15, 17);
INSERT INTO `m_blogtag` VALUES (16, 19);
INSERT INTO `m_blogtag` VALUES (12, 20);

-- ----------------------------
-- Table structure for m_friend
-- ----------------------------
DROP TABLE IF EXISTS `m_friend`;
CREATE TABLE `m_friend`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `href` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '链接地址',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '头像地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_friend
-- ----------------------------
INSERT INTO `m_friend` VALUES (1, 'Smile\' Blog', '一个神秘的博客', 'https://blog.areyou.ml/', 'https://avatars.githubusercontent.com/u/28394742');

-- ----------------------------
-- Table structure for m_system
-- ----------------------------
DROP TABLE IF EXISTS `m_system`;
CREATE TABLE `m_system`  (
  `id` int(11) NOT NULL,
  `welcome` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_system
-- ----------------------------
INSERT INTO `m_system` VALUES (1, '欢迎来到Smilex博客', 'Smile\' Blog', '一个神秘的博客');

-- ----------------------------
-- Table structure for m_tag
-- ----------------------------
DROP TABLE IF EXISTS `m_tag`;
CREATE TABLE `m_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_tag
-- ----------------------------
INSERT INTO `m_tag` VALUES (1, '牛劈');
INSERT INTO `m_tag` VALUES (2, '哈哈');
INSERT INTO `m_tag` VALUES (4, '测试');
INSERT INTO `m_tag` VALUES (7, '啊啊啊');
INSERT INTO `m_tag` VALUES (8, '很溜');
INSERT INTO `m_tag` VALUES (9, '很难');
INSERT INTO `m_tag` VALUES (10, '八嘎');
INSERT INTO `m_tag` VALUES (11, '黑河');
INSERT INTO `m_tag` VALUES (12, '哦哦');
INSERT INTO `m_tag` VALUES (13, '嘎嘎嘎');
INSERT INTO `m_tag` VALUES (14, 'markdown');
INSERT INTO `m_tag` VALUES (15, '测试文章');
INSERT INTO `m_tag` VALUES (16, '测试文章5');
INSERT INTO `m_tag` VALUES (17, 'Hello');
INSERT INTO `m_tag` VALUES (18, 'World');
INSERT INTO `m_tag` VALUES (19, '1');
INSERT INTO `m_tag` VALUES (20, '测试文章4');

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
INSERT INTO `m_user` VALUES (1, 'xiaoxiao', 'https://avatars.githubusercontent.com/u/28394742', 'msmliexx1@gmail.com', '123123', 1, '2022-03-29 21:23:55', '2022-04-17 07:45:32');
INSERT INTO `m_user` VALUES (2, 'x', 'https://avatars.githubusercontent.com/u/28394742', 'msmliexx1@gmail.com', '123123', -1, '2022-03-29 21:23:55', '2022-03-29 21:24:00');

SET FOREIGN_KEY_CHECKS = 1;
